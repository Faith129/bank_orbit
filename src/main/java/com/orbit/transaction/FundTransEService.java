package com.orbit.transaction;

import com.orbit.dto.request.TransactionRequest;
import com.orbit.dto.response.TransactionResponse;

public interface FundTransEService {
    TransactionResponse executeFundTransfer(TransactionRequest request);
}
