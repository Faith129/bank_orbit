package com.orbit.transaction;

import com.orbit.dto.request.TransactionRequest;
import com.orbit.exceptions.ResponseModel;
import com.orbit.models.Transactions;


public interface FundTransferOpsService {
    ResponseModel performTransaction(TransactionRequest transferRequest);

}
