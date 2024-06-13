package com.mybank.module3_loan.service;

import com.mybank.module3_loan.model.*;
import com.mybank.module3_loan.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ReviewerService {

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private SavingAccountRepository savingAccountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private UserService userService;

    // 0: PENDING, 1: REJECTED, 2: APPROVED, 3: CONFIRMED
    public static final String PENDING = "PENDING";
    public static final String REJECTED = "REJECTED";
    public static final String APPROVED = "APPROVED";
    public static final String CONFIRMED = "CONFIRMED";

    public void reviewLoanApplication(Long loanApplicationId, boolean approve, String reviewComments, BigDecimal collateralAmount) {
        Optional<LoanApplication> loanApplicationOptional = loanApplicationRepository.findById(loanApplicationId);
        if (loanApplicationOptional.isPresent()) {
            LoanApplication loanApplication = loanApplicationOptional.get();
            Customer customer = customerRepository.findById(loanApplication.getCustomerId())
                    .orElseThrow(() -> new RuntimeException("Customer not found"));
            BigDecimal availableCreditLimit = userService.calculateAvailableCreditLimit(loanApplication.getCustomerId());

            if (loanApplication.getLoanAmount().compareTo(BigDecimal.valueOf(100000)) <= 0) { // 小额贷款
                if (loanApplication.getLoanAmount().compareTo(availableCreditLimit) <= 0) { // 在信用额度内
                } else { // 超出信用额度
                    // 返回所有银行账户的交易记录和信用卡记录以供进一步审核
                    //List<Transaction> transactions = transactionRepository.findByCustomerId(customer.getCustomerId());
                    //List<CreditCard> creditCards = creditCardRepository.findByCustomerId(customer.getCustomerId());
                    // 处理展示逻辑...
                }
                if (approve) {
                    loanApplication.setStatus(APPROVED);
                    loanApplication.setCollateralAmount(BigDecimal.ZERO);
                    loanApplication.setReviewComments(reviewComments);
                    loanApplicationRepository.save(loanApplication);
                    confirmLoan(loanApplication);
                } else {
                    loanApplication.setStatus(REJECTED);
                }
            } else { // 大额贷款
                // 需要抵押，并且需要展示交易记录和信用卡记录
                //List<Transaction> transactions = transactionRepository.findByCustomerId(customer.getCustomerId());
                //List<CreditCard> creditCards = creditCardRepository.findByCustomerId(customer.getCustomerId());
                // 处理展示逻辑...
                if (approve) {
                    loanApplication.setStatus(APPROVED);
                    loanApplication.setCollateralAmount(collateralAmount);
                    loanApplication.setReviewComments(reviewComments);
                } else {
                    loanApplication.setStatus(REJECTED);
                }
            }
            loanApplicationRepository.save(loanApplication);
        }
    }

    private void confirmLoan(LoanApplication loanApplication) {
        loanApplication.setStatus(CONFIRMED);
        loanApplication.setLoanStartDate(LocalDate.now());
        loanApplication.setLoanEndDate(LocalDate.now().plusMonths(loanApplication.getLoanDuration()));
        loanApplication.setTotalRepaymentAmount(loanApplication.getLoanAmount());
        loanApplication.setLastInterestUpdateDate(LocalDate.now());

        // 更新存款账户余额
        SavingAccount savingAccount = savingAccountRepository.findById(loanApplication.getAccountId())
                .orElseThrow(() -> new RuntimeException("Saving account not found"));
        savingAccount.setBalance(savingAccount.getBalance() + loanApplication.getLoanAmount().doubleValue());
        savingAccountRepository.save(savingAccount);

        loanApplicationRepository.save(loanApplication);
    }

    public LoanApplication getLoanApplicationDetails(Long loanApplicationId) {
        return loanApplicationRepository.findById(loanApplicationId).orElse(null);
    }

    public List<LoanApplication> getAllLoanApplications() {
        return loanApplicationRepository.findAll();
    }

    public Customer getCustomerDetails(Long customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }

    public List<SavingAccount> getSavingAccounts(Long customerId) {
        return savingAccountRepository.findByCustomerId(customerId);
    }

    public BigDecimal calculateMonthlyNetIncome(Long customerId) {
        List<Transaction> transactions = getTransactionsByCustomerId(customerId);

        BigDecimal totalIncome = transactions.stream()
                .filter(transaction -> transaction.getCardId().equals(transaction.getMoneyGoes()))
                .map(Transaction::getTransactionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = transactions.stream()
                .filter(transaction -> transaction.getCardId().equals(transaction.getMoneySource()))
                .map(Transaction::getTransactionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return totalIncome.subtract(totalExpenses);
    }

    public long calculateMonthlyTransactionCount(Long customerId) {
        List<Transaction> transactions = getTransactionsByCustomerId(customerId);

        return transactions.stream()
                .map(transaction -> transaction.getTransactionTime().withDayOfMonth(1))
                .distinct()
                .count();
    }

    private List<Transaction> getTransactionsByCustomerId(Long customerId) {
        List<SavingAccount> accounts = savingAccountRepository.findByCustomerId(customerId);
        List<String> accountIds = accounts.stream()
                .map(SavingAccount::getAccountId)
                .collect(Collectors.toList());

        return transactionRepository.findByCardIdIn(accountIds);
    }

    public BigDecimal getTotalSavings(Long customerId) {
        List<SavingAccount> savingAccounts = savingAccountRepository.findByCustomerId(customerId);
        return savingAccounts.stream()
                .map(savingAccount -> BigDecimal.valueOf(savingAccount.getBalance()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal getTotalCreditCardDebt(Long customerId) {
        List<CreditCard> creditCards = creditCardRepository.findByCustomerId(customerId);
        return creditCards.stream()
                .map(CreditCard::getConsumption)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }


    public BigDecimal getAvailableCollateral(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            return BigDecimal.ZERO;
        }
        return customer.getAssets(); // Assuming assets can be used as collateral
    }

    public com.example.loan.model.CustomerFinancialSummary getCustomerFinancialSummary(Long customerId) {
        BigDecimal monthlyNetIncome = calculateMonthlyNetIncome(customerId);
        long monthlyTransactionCount = calculateMonthlyTransactionCount(customerId);
        BigDecimal totalSavings = getTotalSavings(customerId);
        BigDecimal totalCreditCardDebt = getTotalCreditCardDebt(customerId);
        BigDecimal availableCollateral = getAvailableCollateral(customerId);

        return new com.example.loan.model.CustomerFinancialSummary(monthlyNetIncome, monthlyTransactionCount, totalSavings, totalCreditCardDebt, availableCollateral);
    }
}
