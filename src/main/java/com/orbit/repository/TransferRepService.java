package com.orbit.repository;

import com.orbit.dto.response.TransactionResponse;

public interface TransferRepService {
    boolean isFundTransferDirectDebitSaved(TransactionResponse response);
    void saveIntoFundTransfer(TransactionResponse response);
}
