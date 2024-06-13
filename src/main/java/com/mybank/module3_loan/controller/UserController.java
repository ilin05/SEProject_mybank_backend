package com.mybank.module3_loan.controller;

import com.mybank.module3_loan.model.*;
import com.mybank.module3_loan.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/home/loan")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/updateRepaymentStatus/{loanApplicationId}")
    public ResponseEntity<Void> updateRepaymentStatus(@PathVariable Long loanApplicationId) {
        LoanApplication loanApplication = userService.getLoanApplicationById(loanApplicationId);
        if (loanApplication != null) {
            userService.updateRepaymentStatus(loanApplication);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/updatePendingAndOverdueRepaymentStatuses/{customerId}")
    public ResponseEntity<Void> updatePendingAndOverdueLoanRepaymentStatuses(@PathVariable Long customerId) {
        userService.updatePendingAndOverdueLoanRepaymentStatuses(customerId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/repay/card")
    public ResponseEntity<List<SavingAccount>> getSavingAccountsByCustomerIdAndBalanceGreaterThan(
            @RequestParam Long customerId, @RequestParam double balance) {
        List<SavingAccount> accounts = userService.findSavingAccountsByCustomerIdAndBalanceGreaterThan(customerId, balance);
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/login")
    public ResponseEntity<SavingAccount> login(@RequestParam String accountId, @RequestParam String password) {
        SavingAccount account = userService.login(accountId, password);
        if (account != null) {
            return ResponseEntity.ok(account);
        }
        return ResponseEntity.status(401).build();
    }

    @GetMapping("/customer/{idNumber}")
    public ResponseEntity<Customer> getCustomerByIdNumber(@PathVariable String idNumber) {
        Customer customer = userService.findCustomerByIdNumber(idNumber);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        }
        return ResponseEntity.notFound().build();
    }

    //@GetMapping("/customer/{customerId}/accounts") sbmg
    @PostMapping("/application/card")
    public ResponseEntity<List<SavingAccount>> getSavingAccountsByCustomerId(@RequestParam Long customerId) {
        List<SavingAccount> accounts = userService.findSavingAccountsByCustomerId(customerId);
        return ResponseEntity.ok(accounts);
    }

    @PostMapping("/application/apply")
    public ResponseEntity<String> applyForLoan(@RequestBody LoanApplicationRequest loanApplicationRequest) {
        LoanApplication loanApplication = userService.applyForLoan(
                loanApplicationRequest.getCustomerId(),
                loanApplicationRequest.getLoanAmount(),
                loanApplicationRequest.getLoanDuration(),
                loanApplicationRequest.getAccountId(),
                loanApplicationRequest.getLoanType()
        );
        return ResponseEntity.ok("success");
    }

    @GetMapping("/creditLimit/{customerId}")
    public ResponseEntity<BigDecimal> getCreditLimit(@PathVariable Long customerId) {
        BigDecimal creditLimit = userService.calculateCreditLimit(customerId);
        return ResponseEntity.ok(creditLimit);
    }

    //获取所有待审核的贷款记录
    @PostMapping("/withdraw")
    public ResponseEntity<List<LoanApplication>> getPendingLoanApplicationsByCustomerId(@RequestParam Long customerId) {
        List<LoanApplication> pendingLoans = userService.getPendingLoanApplicationsByCustomerId(customerId);
        return ResponseEntity.ok(pendingLoans);
    }

    @PostMapping("/pendingOrRecentLoans")
    public ResponseEntity<List<LoanApplication>> getPendingOrRecentLoans(
            @RequestParam Long customerId, @RequestParam int days) {
        List<LoanApplication> loans = userService.getPendingOrRecentLoans(customerId, days);
        return ResponseEntity.ok(loans);
    }

    @PostMapping("/history")
    public ResponseEntity<List<LoanApplication>> getUserLoanApplications(@RequestParam Long customerId) {
        List<LoanApplication> applications = userService.getUserLoanApplications(customerId);
        return ResponseEntity.ok(applications);
    }

    @PostMapping("/withdraw/delete")
    public ResponseEntity<String> withdrawLoanApplication(@RequestParam Long loanId) {
        try {
            userService.withdrawLoanApplication(loanId);
            return ResponseEntity.ok("success");
        } catch (RuntimeException e) {
            return ResponseEntity.ok("failed");
        }
    }


    @GetMapping("/loan/{loanApplicationId}/repayments")
    public ResponseEntity<List<Repayment>> getRepaymentRecords(@PathVariable Long loanApplicationId) {
        List<Repayment> repayments = userService.getRepaymentRecords(loanApplicationId);
        return ResponseEntity.ok(repayments);
    }

    @PostMapping("/repay")
    public ResponseEntity<List<LoanApplication>> getConfirmedPendingRepayments(@RequestParam Long customerId) {
        List<LoanApplication> loans = userService.getConfirmedPendingRepayments(customerId);
        return ResponseEntity.ok(loans);
    }

    @PostMapping("/repay/pay")
    public ResponseEntity<String> repayLoan(
            @RequestParam Long loanId,
            @RequestParam BigDecimal repaymentAmount,
            @RequestParam String accountId) {
        Repayment repayment = userService.repayLoan(loanId, repaymentAmount, accountId);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/credit/{userId}")
    public ResponseEntity<BigDecimal> calculateCreditLimit(@PathVariable Long userId) {
        BigDecimal creditLimit = userService.calculateCreditLimit(userId);
        return ResponseEntity.ok(creditLimit);
    }

    @PostMapping("/withdraw/confirm")
    public ResponseEntity<String> confirmLoan(@RequestParam Long loanId) {
        userService.confirmLoan(loanId);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/application/credit")
    public ResponseEntity<BigDecimal> getAvailableCreditLimit(@RequestParam Long customerId) {
        BigDecimal availableCreditLimit = userService.calculateAvailableCreditLimit(customerId);
        return ResponseEntity.ok(availableCreditLimit);
    }
}
