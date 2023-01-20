package com.orbit.transaction;

import com.orbit.dto.request.TransactionRequest;
import com.orbit.dto.response.TransactionResponse;
import com.orbit.enums.TransactionType;

public interface TransferOpsService {
    TransactionResponse doTransfer(TransactionRequest request);
    boolean hasSavedFundTransfer(TransactionResponse fdr, TransactionType requestType);
}
