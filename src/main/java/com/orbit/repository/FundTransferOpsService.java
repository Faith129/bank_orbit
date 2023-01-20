package com.orbit.repository;

import com.orbit.dto.request.TransactionRequest;
import com.orbit.dto.response.TSQResponse;
import com.orbit.exceptions.ResponseModel;
import com.orbit.models.TransactionProperties;
import com.orbit.models.Transactions;


public interface FundTransferOpsService {
    ResponseModel performTransaction(TransactionProperties transferRequest);
    boolean saveTSQ(TSQResponse response);

}
