package com.orbit.transaction.Impl;

import com.orbit.dto.request.TransactionRequest;
import com.orbit.dto.response.TransactionResponse;
import com.orbit.enums.ResponseCode;
import com.orbit.enums.TransactionType;
import com.orbit.transaction.FundTransEService;
import com.orbit.transaction.TransactionsService;
import com.orbit.transaction.TransferOpsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.concurrent.Executors;

@RequiredArgsConstructor
@Service
public class FundTransExecutionImpl implements FundTransEService {

    private final TransferOpsService transferOpsService;
    private static final int TIME_DELAY_IN_MILLIS = 60_000;
    private final TransactionsService transactionsService;
    @Override
    public TransactionResponse executeFundTransfer(TransactionRequest request) {
        TransactionResponse response = buildResponse(request);
//        boolean isFundTransferSaved = savedFundTransfer(response, TransactionType.DEBIT);
//
//        if (!isFundTransferSaved) {response.setResponseCode(ResponseCode.BAD_REQUEST.getCanonicalCode());}

//        if (response.getResponseCode().equals(ResponseCode.APPROVED_OR_COMPLETED_SUCCESSFULLY)) {

            Executors.newSingleThreadExecutor()
                    .submit( () -> {
                        try {
                            Thread.sleep(TIME_DELAY_IN_MILLIS);
                            transactionsService.doFundTransfer(response, TransactionType.DEBIT);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    });

        return response;
    }

    private TransactionResponse buildResponse(TransactionRequest requests) {

        TransactionResponse response = new TransactionResponse();
        response.setSessionId(response.getSessionId());
        response.setNameEnquiryRef(requests.getNameEnquiryRef());
        response.setDestinationInstitutionCode(requests.getDestinationInstitutionCode());
        response.setChannelCode(requests.getChannelCode());
        response.setBeneficiaryAccountName(requests.getBeneficiaryAccountName());
        response.setToAccountNumber(requests.getToAccountNumber());
        response.setBeneficiaryBankVerificationNumber(requests.getBeneficiaryBankVerificationNumber());
        response.setOriginatorAccountName(requests.getOriginatorAccountName());
        response.setFromAccountNumber(requests.getToAccountNumber());
        response.setDebitBankVerificationNumber(requests.getDebitBankVerificationNumber());
        response.setTransactionRef(requests.getTransactionRef());
        response.setTransAmount(requests.getTransAmount());

        return response;
    }



    public boolean savedFundTransfer(TransactionResponse response, TransactionType debit) {
        return transferOpsService.hasSavedFundTransfer(response, debit);
    }

}
