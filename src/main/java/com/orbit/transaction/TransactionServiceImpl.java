package com.orbit.transaction;

import com.orbit.dto.request.TransactionRequest;
import com.orbit.dto.response.TransactionResponse;
import com.orbit.enums.TransactionType;
import com.orbit.exceptions.ResponseConstants;
import com.orbit.exceptions.ResponseModel;
import com.orbit.models.Transactions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

import static com.orbit.exceptions.ResponseCode.APPROVED_OR_COMPLETED_SUCCESSFULLY;
import static com.orbit.exceptions.ResponseCode.SYSTEM_MALFUNCTION;
@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionsService {

    private final FundTransferOpsService fundTransferService;

    @Override
    public TransactionResponse doFundTransfer(TransactionRequest request, TransactionType transType) {

        String fromAccountNumber = "", toAccountNumber = "", debitCreditFlag = "";


        TransactionResponse response = buildResponseFrom(request);
        if (response.getResponseCode().equals(APPROVED_OR_COMPLETED_SUCCESSFULLY)) {
            BigDecimal transferAmount = request.getTransAmount();

            if (transType.equals(TransactionType.CREDIT)) {
                fromAccountNumber = response.getFromAccountNumber();
                toAccountNumber = response.getToAccountNumber();
                debitCreditFlag = "CR";
            }
            if (transType.equals(TransactionType.DEBIT)) {
                toAccountNumber = response.getToAccountNumber();
                fromAccountNumber = response.getFromAccountNumber();
                debitCreditFlag = "DR";
            }

            TransactionRequest transferRequest = TransactionRequest.builder()
               .fromAccountNumber(fromAccountNumber)
                .toAccountNumber(toAccountNumber)
                .debitCrditFlag(debitCreditFlag)
                .transAmount(transferAmount)
                .narration(request.getNarration())
                .transactionRef(request.getTransactionRef())
                .toAccountNumber(request.getToAccountNumber())
                .isReversal(true)
                .build();

            ResponseModel responseModel = fundTransferService.performTransaction(transferRequest);
            if (!responseModel.getResponseCode().trim().equals(ResponseConstants.SUCCEESS_CODE)){
                response.setResponseCode(SYSTEM_MALFUNCTION);
            }
          // directDebitOps.saveIntoFundTransferDirectDebit(response);
        }


        return response;
    }


    private TransactionResponse buildResponseFrom(TransactionRequest request) {
        TransactionResponse response = new TransactionResponse();
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



}
