package com.orbit.repository.impl;

import com.orbit.dto.response.TSQResponse;
import com.orbit.exceptions.ResponseConstants;
import com.orbit.exceptions.ResponseModel;
import com.orbit.models.TransactionProperties;
import com.orbit.repository.FundTransferOpsService;
import com.orbit.transaction.BalanceEnquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.Map;
@RequiredArgsConstructor
@Service
public class FundTransferOperationsImpl implements FundTransferOpsService {

    public final String SAVE_TRANSACTION_STATUS_QUERY = "INSERT INTO nip_tsq(session_id, source_institution_code, channel_code, response_code) values(?, ?, ?, ?)";
    private final JdbcTemplate jdbcTemplate;
private final BalanceEnquiryService balanceEnquiryService;
    @Override
    public boolean saveTSQ(TSQResponse response) {
        jdbcTemplate.update(SAVE_TRANSACTION_STATUS_QUERY, response.getSessionId(), response.getSourceInstitutionCode(),
                response.getChannelCode(), response.getResponseCode());
        return false;
    }


   @Transactional
    @Override
   public ResponseModel performTransaction(TransactionProperties properties){
        Map<String, Object> result = procedureExecution(properties);
       jdbcTemplate.update("SET CHAINED ON");
       String errorCode = result.get("pnErrorCode").toString();
        String errorMessage = "";
        if (result != null) {
            errorCode = (String) result.get("pnErrorCode");
            errorMessage = (String) result.get("psErrorMessage");

        }

        ResponseModel response = new ResponseModel();
        if (errorCode.trim().equals("0")) {
            response.setResponseCode(ResponseConstants.SUCCEESS_CODE);
            response.setResponseMessage(ResponseConstants.SUCCEESS_MESSAGE);
            response.setRemark("Txn Debit of " + properties.getTransAmount() +" from " + properties.getFromAccountNumber() + " to " + properties.getToAccountNumber());

        }
        return response;
    }
    private Map<String, Object> procedureExecution(TransactionProperties transferRequest) {
        int initiatingApp = 96;
        int chargeCode = 660;
        String reversalFlag = transferRequest.isReversal() ? "Y" : "N";
        BigDecimal chargeAmount = BigDecimal.ZERO;
        BigDecimal taxAmount = BigDecimal.ZERO;

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("csp_mapp_TranPosting");
        SqlParameterSource inputParams = new MapSqlParameterSource().addValue("psInitiatingApp", "97", Types.INTEGER)
            .addValue("psInitiatingApp", initiatingApp, Types.INTEGER)
            .addValue("psFromAccountNumber", transferRequest.getFromAccountNumber(), Types.VARCHAR)
            .addValue("psToAccountNumber", transferRequest.getToAccountNumber(), Types.VARCHAR)
            .addValue("pnTransactionAmount", transferRequest.getTransAmount(), Types.DECIMAL)
            .addValue("psTransactionDescription", transferRequest.getNarration(), Types.VARCHAR)
            .addValue("psTransactionReference", transferRequest.getTransactionRef(), Types.VARCHAR)
            .addValue("pnChargeCode", chargeCode, Types.SMALLINT)
            .addValue("pnChargeAmount", chargeAmount, Types.DECIMAL)
            .addValue("pnTaxAmount", taxAmount, Types.DECIMAL)
            .addValue("psDebitCredit", transferRequest.getDebitCreditFlag(), Types.VARCHAR)
            .addValue("psReversalFlg", reversalFlag, Types.VARCHAR)
            .addValue("pnErrorCode", 0, Types.INTEGER)
            .addValue("psErrorMessage", "", Types.VARCHAR);

        System.out.println("==============================================");
        System.out.println("ACCOUNT FROM " + transferRequest.getFromAccountNumber() + " ACCOUNT TO " + transferRequest.getToAccountNumber() +
                " TRANSACTION AMOUNT " +    transferRequest.getTransAmount());

        System.out.println("ACCOUNT BALANCE IN ACCOUNT " + transferRequest.getFromAccountNumber() + " AFTER TRANSACTION IS " + balanceEnquiryService.getBalance(
                transferRequest.getFromAccountNumber()).getAvailableBalance());

        return simpleJdbcCall.execute(inputParams);


    }




}


