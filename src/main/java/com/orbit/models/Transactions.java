package com.orbit.models;

import lombok.*;


import java.math.BigDecimal;
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @Builder
    public class Transactions {
        private Long id;
        private String sessionId;
        private String transactionRef;
        private String accountNo;
        private String tranAppl;
        private String fromAccountNumber;
        private String toAccountNumber;
        private String transReceiver;
        private BigDecimal transAmount;
        private String tranStatus;
        private String narration;
        private String transactionType;
        private String transactionDate;
        private BigDecimal balanceEnquiries;
        private String debitCrditFlag;
        private boolean isReversal;
        private String destinationInstitutionCode;
        private int channelCode;
        private String responseCode;
        private BigDecimal transactionFee;
        private String nameEnquiryRef;
        private String beneficiaryBankVerificationNumber;
        private String beneficiaryKYCLevel;
        private String debitAccountName;
        private String debitBankVerificationNumber;

    }
