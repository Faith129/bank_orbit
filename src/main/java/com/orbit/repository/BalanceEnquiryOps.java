package com.orbit.repository;


import com.orbit.models.mapper.BalanceEnquiry;

import java.util.List;

public interface BalanceEnquiryOps {
    List<BalanceEnquiry> queryAccountBalance(final String accountNumber);

}
