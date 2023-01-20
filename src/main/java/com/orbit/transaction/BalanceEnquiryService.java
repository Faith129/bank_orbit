package com.orbit.transaction;

import com.orbit.dto.response.BalanceEnquiryResponse;

public interface BalanceEnquiryService {
    BalanceEnquiryResponse getBalance(String accountNumber);
}
