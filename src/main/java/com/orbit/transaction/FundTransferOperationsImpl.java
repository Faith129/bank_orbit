package com.orbit.transaction;

import com.orbit.dto.request.TransactionRequest;
import com.orbit.exceptions.ResponseConstants;
import com.orbit.exceptions.ResponseModel;
import com.orbit.models.Transactions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.sql.Types;
import java.util.Map;

@Service
public class FundTransferOperationsImpl implements FundTransferOpsService {
    @Autowired
    JdbcTemplate jdbcTemplate;

   @Transactional
    @Override
   public ResponseModel performTransaction(TransactionRequest transferRequest){
        Map<String, Object> result = procedureExecution(transferRequest);

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
            response.setRemark("Txn Debit of " + transferRequest.getTransAmount() +" from " + transferRequest.getFromAccountNumber() + " to " + transferRequest.getToAccountNumber());

        }
        return response;
    }

    private Map<String, Object> procedureExecution(TransactionRequest transferRequest) {
        int chargeCode = 660;
        BigDecimal chargeAmount = BigDecimal.ZERO;
        BigDecimal taxAmount = BigDecimal.ZERO;

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(jdbcTemplate).withProcedureName("csp_mapp_TranPosting");
        SqlParameterSource inputParams = new MapSqlParameterSource().addValue("psInitiatingApp", "97", Types.INTEGER)
            .addValue("psFromAccountNumber", transferRequest.getFromAccountNumber(), Types.VARCHAR)
            .addValue("psToAccountNumber", transferRequest.getToAccountNumber(), Types.VARCHAR)
            .addValue("pnTransactionAmount", transferRequest.getTransAmount(), Types.DECIMAL)
            .addValue("psTransactionDescription", transferRequest.getNarration(), Types.VARCHAR)
            .addValue("psTransactionReference", transferRequest.getTransactionRef(), Types.VARCHAR)
            .addValue("pnChargeCode", chargeCode, Types.SMALLINT)
            .addValue("pnChargeAmount", chargeAmount, Types.DECIMAL)
            .addValue("pnTaxAmount", taxAmount, Types.DECIMAL)
            .addValue("psDebitCredit", transferRequest.getDebitCrditFlag(), Types.VARCHAR)
            .addValue("psReversalFlg", "N", Types.VARCHAR)
            .addValue("pnErrorCode", 0, Types.INTEGER)
            .addValue("psErrorMessage", "", Types.VARCHAR);

        return simpleJdbcCall.execute(inputParams);

    }




}


