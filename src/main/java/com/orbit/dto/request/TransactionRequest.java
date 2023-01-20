package com.orbit.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionRequest {
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
    private LocalDate transactionDate;
    private String debitCrditFlag;
    private String responseCode;
    private boolean isReversal;
    private String destinationInstitutionCode;
    private int channelCode;
    private BigDecimal transactionFee;
    private String nameEnquiryRef;
    private String beneficiaryBankVerificationNumber;
    private String beneficiaryKYCLevel;
    private String debitAccountName;
    private String debitBankVerificationNumber;
    private String beneficiaryAccountName;
    private String originatorAccountName;


}
