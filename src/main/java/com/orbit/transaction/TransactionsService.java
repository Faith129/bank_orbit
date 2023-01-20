package com.orbit.transaction;

import com.orbit.dto.request.TransactionRequest;
import com.orbit.dto.response.TransactionResponse;
import com.orbit.enums.ResponseCode;
import com.orbit.enums.TransactionType;
import com.orbit.exceptions.ResponseModel;
import com.orbit.models.TransactionProperties;

public interface TransactionsService {
    TransactionResponse doFundTransfer(TransactionResponse response, TransactionType transType) ;
    ResponseModel postTransaction(TransactionProperties properties);
    ResponseCode performFundTransfer(TransactionRequest request);
}
