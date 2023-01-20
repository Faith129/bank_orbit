package com.orbit.repository.impl;

import com.orbit.dto.response.TransactionResponse;
import com.orbit.repository.TransferRepService;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
@RequiredArgsConstructor
@Service
public class TransferRepo implements TransferRepService {
    public static final String SAVE_NIP_TSQ = "INSERT INTO nip_tsq(session_id, source_institution_code, channel_code, response_code) values(?, ?, ?, ?)";

    public static final String SAVE_INTO_NIP_XTER_DIRECT_CREDIT = "insert into nip_xfer_direct_debit(session_id, name_enquiry_ref, destination_institution_code, channel_code, ben_account_name,\r\n" +
            " ben_account_number, ben_bvn, ben_kyclevel, deb_account_name, deb_account_number, deb_bvn, deb_kyclevel, transaction_location,\r\n" +
            " narration, payment_reference, mandate_reference_number, transaction_fee, amount, response_code)\r\n" +
            " values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

private final JdbcTemplate jdbcTemplate;
    @Override
    public boolean isFundTransferDirectDebitSaved(TransactionResponse response) {
        return jdbcTemplate.update(SAVE_NIP_TSQ, response.getSessionId(), response.getDestinationInstitutionCode(),
                response.getChannelCode(), response.getResponseCode()) == 0;
    }

    @Override
    public void saveIntoFundTransfer(TransactionResponse response) {
        jdbcTemplate.update(SAVE_INTO_NIP_XTER_DIRECT_CREDIT, response.getSessionId(),
                response.getNameEnquiryRef(), response.getDestinationInstitutionCode(),
                response.getChannelCode(), response.getToAccountNumber(),
                response.getToAccountNumber(), response.getBeneficiaryBankVerificationNumber(),
                response.getDebitAccountName(),  response.getFromAccountNumber(), response.getDebitBankVerificationNumber(),
                response.getNarration(), response.getTransactionRef(),
                response.getTransactionFee(), response.getTransactionFee(),
                response.getTransAmount(), response.getResponseCode());
    }

}
