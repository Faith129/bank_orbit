package com.orbit.controller;

//import com.orbit.dto.request.AccountRequest;
import com.orbit.dto.request.TransactionRequest;
//import com.orbit.dto.response.ServiceResponse;
//import com.orbit.services.transaction.TransactionsService;
//import com.orbit.services.transactionsOld.AccountService;
//import com.orbit.services.transactionsOld.BalanceService;
//import com.orbit.services.transactionsOld.TransactionService;
//import io.swagger.annotations.ApiOperation;
import com.orbit.transaction.BalanceEnquiryService;
import com.orbit.transaction.FundTransEService;
import com.orbit.transaction.TransactionsService;
import com.orbit.transaction.TransferOpsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

//import javax.validation.Valid;
@RequiredArgsConstructor
@CrossOrigin
@RestController
//@RequestMapping("/api/v1")
public class ServiceController {
//@Autowired
//private BalanceService balanceService;
//
//@Autowired
//private TransactionService transactionService;
//
@Autowired
BalanceEnquiryService balanceEnquiryService;
    private final TransactionsService transactionsService;
  private final TransferOpsService transferOpsService;
    private final FundTransEService fundTransEService;
//private final BalanceEnquiryService balanceEnquiryService;

//    @Autowired
//    FundTransferOpsService fundTransferOpsService;

    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@Valid @RequestBody TransactionRequest request)  {
        return ResponseEntity.ok(fundTransEService.executeFundTransfer(request));
    }

    @GetMapping("/balance/{accountNumber}")
    public ResponseEntity<?> getBalance(@PathVariable String accountNumber)  {
        return ResponseEntity.ok(balanceEnquiryService.getBalance(accountNumber));
    }
//
//    @GetMapping("/balance/{accountNo}")
//    @ApiOperation(value = "Balance enquiry")
//    public ResponseEntity<ServiceResponse> getBalance(@PathVariable String accountNo) {
//        return ResponseEntity.ok(balanceService.balanceEnquiry(accountNo));
//    }
//
//    @PostMapping("/deposit")
//    @ApiOperation(value = "credit account")
//    public ResponseEntity<ServiceResponse> deposit(@Valid @RequestBody TransactionRequest request) {
//        return ResponseEntity.ok(transactionService.deposit(request));
//    }
//
//    @PostMapping("/transfer")
//    @ApiOperation(value = "debit account")
//    public ResponseEntity<?> transfer(@Valid @RequestBody TransactionRequest request) throws AccountNotFoundException {
//        return ResponseEntity.ok(transactionService.amountTransfer(request));
//    }
//
//    @PostMapping("/withdraw")
//    @ApiOperation(value = "debit account")
//    public ResponseEntity<ServiceResponse> withdraw(@Valid @RequestBody TransactionRequest request) {
//        return ResponseEntity.ok(transactionService.withdraw(request));
//    }
//
//    @PostMapping("/addAccount")
//    @ApiOperation(value = "create account endpoint")
//    public ResponseEntity<ServiceResponse> createAccount(@Valid @RequestBody AccountRequest accountRequest) {
//        return ResponseEntity.ok(accountService.createAccount(accountRequest));
//    }
//
//    @GetMapping("/account_info/{accountNo}")
//    @ApiOperation(value = "get account information")
//    public ResponseEntity<ServiceResponse> getAccountInfo(@PathVariable String accountNo) {
//        return ResponseEntity.ok(accountService.getAccountInfo(accountNo));
//    }
}
