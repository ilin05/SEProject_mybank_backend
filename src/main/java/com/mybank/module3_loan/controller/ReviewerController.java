package com.mybank.module3_loan.controller;

import com.mybank.module3_loan.model.Customer;
import com.mybank.module3_loan.model.CustomerFinancialSummary;
import com.mybank.module3_loan.model.LoanApplication;
import com.mybank.module3_loan.model.SavingAccount;
import com.mybank.module3_loan.service.ReviewerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/home/examine")
public class ReviewerController {

    @Autowired
    private ReviewerService reviewerService;

    @PostMapping("/confirm")
    public ResponseEntity<String> reviewLoan(
            @RequestParam Long loanId,
            @RequestParam Long reviewerId,
            @RequestParam String status,
            @RequestParam String reviewComments,
            @RequestParam BigDecimal collateralAmount) {
        boolean result = reviewerService.reviewLoan(loanId, reviewerId, status, reviewComments, collateralAmount);
        if (result) {
            return ResponseEntity.ok("success");
        } else {
            return ResponseEntity.ok("failed");
        }
    }

    @PostMapping("/list")
    public ResponseEntity<List<LoanApplication>> getPendingLoansByReviewerId(@RequestParam Long reviewerId) {
        List<LoanApplication> pendingLoans = reviewerService.getPendingLoansByReviewerId(reviewerId);
        return ResponseEntity.ok(pendingLoans);
    }

    @PostMapping("/detail")
    public ResponseEntity<CustomerFinancialSummary> getCustomerFinancialSummary(@RequestParam Long customerId) {
        CustomerFinancialSummary summary = reviewerService.getCustomerFinancialSummary(customerId);
        return ResponseEntity.ok(summary);
    }

    @GetMapping("/customer/{customerId}/monthlyNetIncome")
    public ResponseEntity<BigDecimal> getMonthlyNetIncome(@PathVariable Long customerId) {
        BigDecimal monthlyNetIncome = reviewerService.calculateMonthlyNetIncome(customerId);
        return ResponseEntity.ok(monthlyNetIncome);
    }

    @GetMapping("/customer/{customerId}/monthlyTransactionCount")
    public ResponseEntity<Long> getMonthlyTransactionCount(@PathVariable Long customerId) {
        long transactionCount = reviewerService.calculateMonthlyTransactionCount(customerId);
        return ResponseEntity.ok(transactionCount);
    }

    @GetMapping("/customer/{customerId}/totalSavings")
    public ResponseEntity<BigDecimal> getTotalSavings(@PathVariable Long customerId) {
        BigDecimal totalSavings = reviewerService.getTotalSavings(customerId);
        return ResponseEntity.ok(totalSavings);
    }

    @GetMapping("/customer/{customerId}/totalCreditCardDebt")
    public ResponseEntity<BigDecimal> getTotalCreditCardDebt(@PathVariable Long customerId) {
        BigDecimal totalCreditCardDebt = reviewerService.getTotalCreditCardDebt(customerId);
        return ResponseEntity.ok(totalCreditCardDebt);
    }

    @GetMapping("/customer/{customerId}/availableCollateral")
    public ResponseEntity<BigDecimal> getAvailableCollateral(@PathVariable Long customerId) {
        BigDecimal availableCollateral = reviewerService.getAvailableCollateral(customerId);
        return ResponseEntity.ok(availableCollateral);
    }

    @PostMapping("/reviewLoanApplication")
    public ResponseEntity<String> reviewLoanApplication(
            @RequestParam Long loanApplicationId,
            @RequestParam boolean approve,
            @RequestParam String reviewComments,
            @RequestParam(required = false) BigDecimal collateralAmount) {

        try {
            reviewerService.reviewLoanApplication(loanApplicationId, approve, reviewComments, collateralAmount);
            return ResponseEntity.ok("Loan application reviewed successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/loanApplications")
    public ResponseEntity<List<LoanApplication>> getAllLoanApplications() {
        List<LoanApplication> loanApplications = reviewerService.getAllLoanApplications();
        return ResponseEntity.ok(loanApplications);
    }

    @GetMapping("/loanApplication/{loanApplicationId}")
    public ResponseEntity<LoanApplication> getLoanApplicationDetails(@PathVariable Long loanApplicationId) {
        LoanApplication loanApplication = reviewerService.getLoanApplicationDetails(loanApplicationId);
        if (loanApplication != null) {
            return ResponseEntity.ok(loanApplication);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<Customer> getCustomerDetails(@PathVariable Long customerId) {
        Customer customer = reviewerService.getCustomerDetails(customerId);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/customer/{customerId}/accounts")
    public ResponseEntity<List<SavingAccount>> getSavingAccounts(@PathVariable Long customerId) {
        List<SavingAccount> savingAccounts = reviewerService.getSavingAccounts(customerId);
        return ResponseEntity.ok(savingAccounts);
    }
}
