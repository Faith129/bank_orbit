package com.orbit.transaction.Impl;

import com.orbit.dto.response.BalanceEnquiryResponse;
import com.orbit.models.mapper.BalanceEnquiry;
import com.orbit.repository.BalanceEnquiryOps;
import com.orbit.transaction.BalanceEnquiryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class BalanceEnquiryServiceImpl implements BalanceEnquiryService {

  private final BalanceEnquiryOps balanceEnquiryOps;
    @Override
    public BalanceEnquiryResponse getBalance(String accountNumber) {
        BalanceEnquiry enquiry = balanceEnquiryOps
                .queryAccountBalance(accountNumber).get(0);


        BalanceEnquiryResponse response = new BalanceEnquiryResponse();

        response.setAvailableBalance(enquiry.getAccountBalance());
        response.setTargetAccountNumber(accountNumber);

        return response;
    }
}
