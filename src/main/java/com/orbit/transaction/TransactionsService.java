package com.orbit.transaction;

import com.orbit.dto.request.TransactionRequest;
import com.orbit.dto.response.TransactionResponse;
import com.orbit.enums.TransactionType;

public interface TransactionsService {
    TransactionResponse doFundTransfer(TransactionRequest request, TransactionType transType) ;
}
