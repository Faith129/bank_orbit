package com.orbit.transaction.Impl;

import com.orbit.dto.request.TransactionRequest;
import com.orbit.dto.response.TSQResponse;
import com.orbit.dto.response.TransactionResponse;
import com.orbit.enums.ResponseCode;
import com.orbit.enums.TransactionType;
import com.orbit.exceptions.ResponseConstants;
import com.orbit.exceptions.ResponseModel;
import com.orbit.models.TransactionProperties;
import com.orbit.repository.FundTransferOpsService;
import com.orbit.transaction.TransactionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@RequiredArgsConstructor
@Service
public class TransactionServiceImpl implements TransactionsService {

    private final FundTransferOpsService fundTransferService;

    @Override
    public TransactionResponse doFundTransfer(TransactionResponse response, TransactionType transType) {


        TSQResponse tsqResponse = new TSQResponse();
        String fromAccountNumber = "", toAccountNumber = "", debitCreditFlag = "";

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
        TransactionProperties properties = TransactionProperties.builder()
                .fromAccountNumber(fromAccountNumber)
                .toAccountNumber(toAccountNumber)
                .debitCreditFlag(debitCreditFlag)
                .transAmount(response.getTransAmount())
                .narration(response.getNarration())
                .transactionRef(response.getSessionId())
                .narration(response.getNarration())
                .isReversal(false)
                .build();

        postTransaction(properties);
       // fundTransferService.saveTSQ(tsqResponse);

        return response;
    }

    @Override
    public ResponseModel postTransaction(TransactionProperties properties) {
        return fundTransferService.performTransaction(properties);
    }



    @Override
    public ResponseCode performFundTransfer(TransactionRequest request) {

        BigDecimal transactionAmount = request.getTransAmount() != null && request.getTransactionFee() != null ? request.getTransAmount().add(request.getTransactionFee()) : null;
        // call procedure to debit account
        System.out.println(transactionAmount);
        TransactionProperties transaction = TransactionProperties.builder().build();
        transaction.setFromAccountNumber(request.getFromAccountNumber());
        transaction.setToAccountNumber(request.getToAccountNumber());
        transaction.setDebitCreditFlag("DR");
        transaction.setTransAmount(transactionAmount);
        transaction.setNarration(request.getNarration());
        transaction.setTransactionRef(request.getSessionId());
        transaction.setReversal(true);

        ResponseModel responseModel = fundTransferService.performTransaction(transaction);

        return responseModel.getResponseCode().equals(ResponseConstants.SUCCEESS_CODE)
                ? ResponseCode.APPROVED_OR_COMPLETED_SUCCESSFULLY
                : ResponseCode.BAD_REQUEST;

    }










//
//
//
//
//
//
//
//        String fromAccountNumber = "", toAccountNumber = "", debitCreditFlag = "";
//
//
//        TransactionResponse response = buildResponseFrom(request);
//        if (response.getResponseCode().equals(APPROVED_OR_COMPLETED_SUCCESSFULLY)) {
//            BigDecimal transferAmount = request.getTransAmount();
//
//            if (transType.equals(TransactionType.CREDIT)) {
//                fromAccountNumber = response.getFromAccountNumber();
//                toAccountNumber = response.getToAccountNumber();
//                debitCreditFlag = "CR";
//            }
//            if (transType.equals(TransactionType.DEBIT)) {
//                toAccountNumber = response.getToAccountNumber();
//                fromAccountNumber = response.getFromAccountNumber();
//                debitCreditFlag = "DR";
//            }
//
//            TransactionProperties properties = TransactionProperties.builder()
//                    .fromAccountNumber(fromAccountNumber)
//                    .toAccountNumber(toAccountNumber)
//                    .debitCrditFlag(debitCreditFlag)
//                    .transAmount(transferAmount)
//                    .narration(request.getNarration())
//                    .transactionRef(request.getTransactionRef())
//                    .toAccountNumber(request.getToAccountNumber())
//                    .isReversal(true)
//                    .build();
//
////            ResponseModel responseModel = fundTransferService.performTransaction(transferRequest);
////            if (!responseModel.getResponseCode().trim().equals(ResponseConstants.SUCCEESS_CODE)){
////                response.setResponseCode(SYSTEM_MALFUNCTION);
////            }
////          // directDebitOps.saveIntoFundTransferDirectDebit(response);
//
//
//            postTransaction(properties);
//            tsqOutwardOps.saveTSQ(transactionStatusQueryResponse);
//
//            return Marshaller.convertObjectToXmlString(transactionStatusQueryResponse);
//        }
//
//
//
////    private TransactionResponse buildResponseFrom(TransactionRequest request) {
////        TransactionResponse response = new TransactionResponse();
////        response.setTransactionRef(request.getTransactionRef());
////
////        response.setToAccountNumber(request.getToAccountNumber());
////
////        response.setFromAccountNumber(request.getFromAccountNumber());
////
////        response.setTransReceiver(request.getTransReceiver());
////        response.setNarration(request.getNarration());
////        response.setTransAmount(request.getTransAmount());
////        response.setTransactionType(request.getTransactionType());
////      //  response.setResponseCode(validateRequest(request));
////        return response;
////    }
//
//
//    }
}