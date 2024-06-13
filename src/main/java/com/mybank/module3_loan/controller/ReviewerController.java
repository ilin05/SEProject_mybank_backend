package com.mybank.module3_loan.controller;

import com.mybank.module3_loan.model.Customer;
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
@RequestMapping("/reviewer")
public class ReviewerController {

    @Autowired
    private ReviewerService reviewerService;

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
