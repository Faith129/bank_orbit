package com.orbit.transaction.Impl;

import com.orbit.dto.request.TransactionRequest;
import com.orbit.dto.response.TransactionResponse;
import com.orbit.enums.TransactionType;
import com.orbit.exceptions.ResponseConstants;
import com.orbit.exceptions.ResponseModel;
import com.orbit.models.TransactionProperties;
import com.orbit.repository.TransferRepService;
import com.orbit.transaction.TransactionsService;
import com.orbit.transaction.TransferOpsService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Objects;

import static com.orbit.exceptions.ResponseCode.APPROVED_OR_COMPLETED_SUCCESSFULLY;
import static com.orbit.exceptions.ResponseCode.SYSTEM_MALFUNCTION;

@RequiredArgsConstructor
@Service
public class TransferOpsImp implements TransferOpsService {
private final TransferRepService transferRepService;
    private final TransactionsService transactionsService;
    private final JdbcTemplate template;
    public static final String SAVE_NIP_TSQ = "INSERT INTO nip_tsq(session_id, source_institution_code, channel_code, response_code) values(?, ?, ?, ?)";

    @Override
    public TransactionResponse doTransfer(TransactionRequest request) {

        TransactionResponse response = buildResponseFrom(request);
        if (!transferRepService.isFundTransferDirectDebitSaved(response)) {
            response.setResponseCode(SYSTEM_MALFUNCTION);
        }

        if (response.getResponseCode().equals(APPROVED_OR_COMPLETED_SUCCESSFULLY)) {
            BigDecimal totalDebitAmount = request.getTransAmount().add(request.getTransactionFee());
            TransactionProperties properties = TransactionProperties.builder()
                    .isReversal(false)
                    .fromAccountNumber(request.getFromAccountNumber())
                    .debitCreditFlag("DR")
                    .transAmount(totalDebitAmount)
                    .narration(request.getNarration())
                    .transactionRef(request.getSessionId())
                    .toAccountNumber(request.getToAccountNumber())
                    .build();

            ResponseModel responseModel = transactionsService.postTransaction(properties);
            if (!responseModel.getResponseCode().trim().equals(ResponseConstants.SUCCEESS_CODE)){
                response.setResponseCode(SYSTEM_MALFUNCTION);
            }
//            System.out.println("ACCOUNT BALANCE IN ACCOUNT " + request.getFromAccountNumber() + " AFTER TRANSACTION IS " + balanceEnquiryService.getBalance(
//                    request.getFromAccountNumber()).getAvailableBalance());
            transferRepService.saveIntoFundTransfer(response);
        }
        return response;
    }


        private TransactionResponse buildResponseFrom(TransactionRequest request) {
        TransactionResponse response = new TransactionResponse();
        response.setSessionId(request.getSessionId());
        response.setDestinationInstitutionCode(request.getDestinationInstitutionCode());
        response.setChannelCode(request.getChannelCode());
        response.setTransactionRef(request.getTransactionRef());

        response.setToAccountNumber(request.getToAccountNumber());

        response.setFromAccountNumber(request.getFromAccountNumber());

        response.setTransReceiver(request.getTransReceiver());
        response.setNarration(request.getNarration());
        response.setTransAmount(request.getTransAmount());
        response.setTransactionType(request.getTransactionType());
      //  response.setResponseCode(validateRequest(request));
        return response;
    }



@Override
    public boolean hasSavedFundTransfer(TransactionResponse fdr, TransactionType requestType) {
      //  CommonMethods.logContent("To save records");
//        if (fdr.getBeneficiaryBankVerificationNumber().isEmpty()) {
//            fdr.setBeneficiaryBankVerificationNumber("12345678945");
//        }


    if (!fdr.getDebitBankVerificationNumber().isEmpty()) {
            fdr.setDebitBankVerificationNumber("12345678945");
        }

        if(fdr.getBeneficiaryKYCLevel()  == 0) {
            fdr.setBeneficiaryKYCLevel(1);
        }

        if(fdr.getNameEnquiryRef().isEmpty()) {
            fdr.setNameEnquiryRef(fdr.getSessionId());
        }

        if(fdr.getNarration().isEmpty()) {
            fdr.setNarration("NIP Transfer " + fdr.getSessionId());
        }


        String serviceType = requestType == TransactionType.CREDIT ? "CREDIT" : "DEBIT";

        String sql = "insert into nip_xfer_direct_credit(session_id, name_enquiry_ref, destination_institution_code," +
                " channel_code, ben_account_name, ben_account_number, ben_bvn, ben_kyclevel, ori_account_name, " +
                " ori_account_number, ori_bvn, ori_kyclevel, transaction_location, narration, payment_reference," +
                " amount, response_code, service_type) \r\n" +
                "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";


        int result = template.update(sql, 	fdr.getSessionId(),
                fdr.getNameEnquiryRef().isEmpty() ? "1" : fdr.getNameEnquiryRef(),
                fdr.getDestinationInstitutionCode(),
                fdr.getChannelCode(),
                fdr.getBeneficiaryAccountName(),
                fdr.getToAccountNumber(),
                fdr.getBeneficiaryBankVerificationNumber().isEmpty()
                        ? "11111111111" : fdr.getBeneficiaryBankVerificationNumber(),
                fdr.getBeneficiaryKYCLevel() == 0 ? 1 : fdr.getBeneficiaryKYCLevel(),
                fdr.getOriginatorAccountName(),
                fdr.getFromAccountNumber(),
                fdr.getDebitBankVerificationNumber().isEmpty()
                        ? "11111111111" : fdr.getNarration().isEmpty() ? "TRANSFER" : fdr.getNarration(),
                fdr.getTransactionRef().isEmpty() ? fdr.getSessionId() :fdr.getTransactionRef(),
                fdr.getTransAmount(),
                fdr.getResponseCode(),
                serviceType);


        return result >= 0;

    }



}
