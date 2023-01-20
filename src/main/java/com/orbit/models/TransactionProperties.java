package com.orbit.models;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class TransactionProperties {
    private String toAccountNumber;
    private String fromAccountNumber;
    private BigDecimal transAmount;
    private String narration;
    private String transactionRef;
    private boolean isReversal;
    private String debitCreditFlag;
}
