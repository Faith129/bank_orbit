package com.orbit.models.mapper;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
@Data
@Builder
public class BalanceEnquiry {
    private String accountType;
    private BigDecimal accountBalance;
    private String colBalance;
}
