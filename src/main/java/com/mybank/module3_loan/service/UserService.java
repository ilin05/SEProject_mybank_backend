package com.mybank.module3_loan.service;

import com.mybank.module3_loan.model.*;
import com.mybank.module3_loan.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    @Autowired
    private RepaymentRepository repaymentRepository;

    @Autowired
    private SavingAccountRepository savingAccountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CreditCardRepository creditCardRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public static final String PENDING = "PENDING";
    public static final String REJECTED = "REJECTED";
    public static final String APPROVED = "APPROVED";
    public static final String CONFIRMED = "CONFIRMED";

    public static final String MORTGAGE = "购房贷款";
    public static final String CAR_LOAN = "购车贷款";
    public static final String PERSONAL_LOAN = "个人贷款";
    public static final String EDUCATION_LOAN = "助学贷款";
    public static final String BUSINESS_LOAN = "商业贷款";
    public static final String OTHER = "其他";

    public static final String NOT_REPAID = "NOT_REPAID";
    public static final String PAID = "PAID";
    public static final String OVERDUE = "OVERDUE";

    public List<LoanApplication> getPendingOrRecentLoans(Long customerId, int days) {
        LocalDate cutoffDate = LocalDate.now().minusDays(days);
        List<LoanApplication> allLoans = loanApplicationRepository.findByCustomerId(customerId);

        return allLoans.stream()
                .filter(loan -> loan.getStatus().equals(PENDING) || loan.getApplicationDate().isAfter(cutoffDate))
                .collect(Collectors.toList());
    }

    public void updateRepaymentStatus(LoanApplication loanApplication) {
        BigDecimal totalRepaymentAmount = loanApplication.getTotalRepaymentAmount();
        BigDecimal paidAmount = loanApplication.getPaidAmount();
        int loanDuration = loanApplication.getLoanDuration(); // 总月数
        LocalDate loanStartDate = loanApplication.getLoanStartDate();

        if (paidAmount.compareTo(totalRepaymentAmount) >= 0) {
            loanApplication.setRepaymentStatus(PAID);
        } else {
            long monthsElapsed = ChronoUnit.MONTHS.between(loanStartDate, LocalDate.now());
            BigDecimal expectedPaidAmount = totalRepaymentAmount.multiply(BigDecimal.valueOf(monthsElapsed)).divide(BigDecimal.valueOf(loanDuration), 2, RoundingMode.HALF_UP);

            if (paidAmount.compareTo(expectedPaidAmount) < 0) {
                loanApplication.setRepaymentStatus(OVERDUE);
            } else {
                loanApplication.setRepaymentStatus(NOT_REPAID);
            }
        }

        loanApplicationRepository.save(loanApplication);
    }

    public LoanApplication getLoanApplicationById(Long loanApplicationId) {
        return loanApplicationRepository.findById(loanApplicationId).orElse(null);
    }

    public void updatePendingAndOverdueLoanRepaymentStatuses(Long customerId) {
        List<LoanApplication> loans = loanApplicationRepository.findByCustomerIdAndRepaymentStatusIn(customerId, Arrays.asList(NOT_REPAID, OVERDUE));
        for (LoanApplication loan : loans) {
            updateRepaymentStatus(loan);
        }
    }
    public List<SavingAccount> findSavingAccountsByCustomerIdAndBalanceGreaterThan(Long customerId, double balance) {
        return savingAccountRepository.findByCustomerIdAndBalanceGreaterThan(customerId, balance);
    }

    private BigDecimal calculateInterestRate(BigDecimal loanAmount, int loanDuration) {
        BigDecimal baseRate = new BigDecimal("0.05"); // 基础利率
        BigDecimal amountFactor = loanAmount.compareTo(BigDecimal.valueOf(100000)) > 0 ? new BigDecimal("0.02") : new BigDecimal("0.01");
        BigDecimal durationFactor = BigDecimal.valueOf(loanDuration).compareTo(BigDecimal.valueOf(12)) > 0 ? new BigDecimal("0.01") : BigDecimal.ZERO;

        return baseRate.add(amountFactor).add(durationFactor);
    }

    public List<LoanApplication> getConfirmedPendingRepayments(Long customerId) {
        List<LoanApplication> loanApplications = loanApplicationRepository.findByCustomerIdAndStatusAndRepaymentStatus(
                customerId, CONFIRMED, NOT_REPAID);

        loanApplications.forEach(loanApplication -> {
            updateInterest(loanApplication);
        });

        return loanApplications;
    }

    private void updateInterest(LoanApplication loanApplication) {
        LocalDate lastUpdate = loanApplication.getLastInterestUpdateDate();
        LocalDate now = LocalDate.now();
        if (lastUpdate != null && !lastUpdate.isEqual(now)) {
            long daysBetween = lastUpdate.until(now, ChronoUnit.DAYS);
            BigDecimal interest = loanApplication.getTotalRepaymentAmount()
                    .multiply(loanApplication.getInterestRate())
                    .multiply(BigDecimal.valueOf(daysBetween))
                    .divide(BigDecimal.valueOf(365), RoundingMode.HALF_UP);

            loanApplication.setTotalRepaymentAmount(loanApplication.getTotalRepaymentAmount().add(interest));
            loanApplication.setLastInterestUpdateDate(now);
            loanApplicationRepository.save(loanApplication);
        }
    }

//    public List<LoanApplication> getConfirmedPendingRepayments(Long customerId) {
//        return loanApplicationRepository.findByCustomerIdAndStatusAndRepaymentStatus(
//                customerId, CONFIRMED, NOT_REPAID);
//    }

    public List<LoanApplication> getPendingLoanApplicationsByCustomerId(Long customerId) {
        updatePendingAndOverdueLoanRepaymentStatuses(customerId);
        return loanApplicationRepository.findByCustomerIdAndStatus(customerId, PENDING);
    }

    public LoanApplication applyForLoan(Long customerId, BigDecimal loanAmount, int loanDuration, String accountId, String loanType) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));

        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setCustomerId(customerId);
        loanApplication.setLoanAmount(loanAmount);
        loanApplication.setAccountId(accountId);
        loanApplication.setLoanType(loanType);
        loanApplication.setStatus(PENDING);
        loanApplication.setLoanDuration(loanDuration);
        loanApplication.setLoanStartDate(null);
        loanApplication.setLoanEndDate(null);
        loanApplication.setApplicationDate(LocalDate.now());
        loanApplication.setInterestRate(calculateInterestRate(loanAmount, loanDuration));

        return loanApplicationRepository.save(loanApplication);
    }

    public void confirmLoan(Long loanApplicationId) {
        LoanApplication loanApplication = loanApplicationRepository.findById(loanApplicationId).orElseThrow(() -> new RuntimeException("Loan application not found"));
        if (loanApplication.getStatus().equals(APPROVED)) {
            loanApplication.setStatus(CONFIRMED);
            loanApplication.setLoanStartDate(LocalDate.now());
            loanApplication.setLoanEndDate(LocalDate.now().plusMonths(loanApplication.getLoanDuration()));

            loanApplication.setTotalRepaymentAmount(loanApplication.getLoanAmount());
            loanApplication.setLastInterestUpdateDate(LocalDate.now());

            // 更新存款账户余额
            SavingAccount savingAccount = savingAccountRepository.findById(loanApplication.getAccountId()).orElseThrow(() -> new RuntimeException("Saving account not found"));
            savingAccount.setBalance(savingAccount.getBalance() + loanApplication.getLoanAmount().doubleValue());
            savingAccountRepository.save(savingAccount);


            // 从客户资产中扣除抵押金额
            Customer customer = customerRepository.findById(loanApplication.getCustomerId()).orElseThrow(() -> new RuntimeException("Customer not found"));
            if (loanApplication.getCollateralAmount() != null && loanApplication.getCollateralAmount().compareTo(BigDecimal.ZERO) > 0) {
                customer.setAssets(customer.getAssets().subtract(loanApplication.getCollateralAmount()));
                customerRepository.save(customer);
            }

            loanApplicationRepository.save(loanApplication);
        } else {
            throw new RuntimeException("Loan application is not approved");
        }
    }

    public BigDecimal calculateAvailableCreditLimit(Long customerId) {
        BigDecimal creditLimit = calculateCreditLimit(customerId);
        BigDecimal outstandingLoans = loanApplicationRepository.findByCustomerId(customerId).stream()
                .filter(loan -> loan.getStatus().equals(CONFIRMED))
                .map(LoanApplication::getLoanAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return creditLimit.subtract(outstandingLoans);
    }

    public SavingAccount login(String accountId, String password) {
        SavingAccount account = savingAccountRepository.findById(accountId).orElse(null);
        if (account != null && passwordEncoder.matches(password, account.getPassword())) {
            return account;
        }
        return null;
    }

    public Customer findCustomerByIdNumber(String idNumber) {
        return customerRepository.findByIdNumber(idNumber);
    }

    public List<SavingAccount> findSavingAccountsByCustomerId(Long customerId) {
        return savingAccountRepository.findByCustomerId(customerId);
    }

    public List<LoanApplication> getUserLoanApplications(Long customerId) {
        return loanApplicationRepository.findByCustomerId(customerId);
    }

    public void withdrawLoanApplication(Long applicationId) {
        LoanApplication application = loanApplicationRepository.findById(applicationId).orElseThrow();
        if (application.getStatus().equals(PENDING)) {
            loanApplicationRepository.delete(application);
        } else {
            throw new RuntimeException("Cannot withdraw processed application");
        }
    }

    public List<Repayment> getRepaymentRecords(Long loanApplicationId) {
        return repaymentRepository.findByLoanApplicationId(loanApplicationId);
    }

    public Repayment repayLoan(Long loanApplicationId, BigDecimal repaymentAmount, String accountId) {
        LoanApplication loanApplication = loanApplicationRepository.findById(loanApplicationId).orElseThrow(() -> new RuntimeException("Loan application not found"));
        SavingAccount savingAccount = savingAccountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Saving account not found"));

        if (repaymentAmount.compareTo(BigDecimal.valueOf(savingAccount.getBalance())) > 0) {
            throw new RuntimeException("Insufficient funds in the saving account");
        }

        savingAccount.setBalance(savingAccount.getBalance()-repaymentAmount.doubleValue());
        savingAccountRepository.save(savingAccount);

        Repayment repayment = new Repayment();
        repayment.setLoanApplicationId(loanApplicationId);
        repayment.setAmount(repaymentAmount);
        repayment.setAccountId(accountId);
        repayment.setRepaymentDate(LocalDate.now());
        repaymentRepository.save(repayment);

        loanApplication.setPaidAmount(loanApplication.getPaidAmount().add(repaymentAmount));
        if (loanApplication.getPaidAmount().compareTo(loanApplication.getTotalRepaymentAmount()) >= 0) {
            loanApplication.setRepaymentStatus(PAID);

            if (loanApplication.getCollateralAmount() != null && loanApplication.getCollateralAmount().compareTo(BigDecimal.ZERO) > 0) {
                Customer customer = customerRepository.findById(loanApplication.getCustomerId()).orElseThrow(() -> new RuntimeException("Customer not found"));
                customer.setAssets(customer.getAssets().add(loanApplication.getCollateralAmount()));
                customerRepository.save(customer);
            }
        }
        loanApplicationRepository.save(loanApplication);

        return repayment;
    }

    public List<Transaction> getTransactionsByCustomerId(Long customerId) {
        List<SavingAccount> savingAccounts = savingAccountRepository.findByCustomerId(customerId);
        List<String> accountIds = savingAccounts.stream()
                .map(SavingAccount::getAccountId)
                .collect(Collectors.toList());

        return transactionRepository.findByCardIdIn(accountIds);
    }

    public BigDecimal calculateCreditLimit(Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null) {
            return BigDecimal.ZERO;
        }

        // 获取用户所有存款账户的余额总和
        List<SavingAccount> savingAccounts = savingAccountRepository.findByCustomerId(customerId);
        BigDecimal totalBalance = savingAccounts.stream()
                .map(savingAccount -> BigDecimal.valueOf(savingAccount.getBalance()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 减去未偿还的贷款金额
        BigDecimal outstandingLoans = loanApplicationRepository.findByCustomerId(customerId).stream()
                .filter(loan -> loan.getStatus() == CONFIRMED)
                .map(LoanApplication::getLoanAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalBalance = totalBalance.subtract(outstandingLoans);

        // 获取用户所有信用卡的消费金额总和
        List<CreditCard> creditCards = creditCardRepository.findByCustomerId(customerId);
        BigDecimal totalConsumption = creditCards.stream()
                .map(CreditCard::getConsumption)
                .reduce(BigDecimal.ZERO, BigDecimal::add);


        // 计算基础信用额度
        BigDecimal creditLimit = totalBalance.multiply(BigDecimal.valueOf(0.7));

        // 获取用户交易流水并计算月均净收入和交易流水量
        List<Transaction> transactions = getTransactionsByCustomerId(customerId);
        // 计算收入和支出
        BigDecimal totalIncome = transactions.stream()
                .filter(transaction -> transaction.getCardId().equals(transaction.getMoneyGoes()))
                .map(Transaction::getTransactionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalExpenses = transactions.stream()
                .filter(transaction -> transaction.getCardId().equals(transaction.getMoneySource()))
                .map(Transaction::getTransactionAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal netIncome = totalIncome.subtract(totalExpenses);
        long months = transactions.stream()
                .map(transaction -> transaction.getTransactionTime().withDayOfMonth(1))
                .distinct()
                .count();
        BigDecimal averageMonthlyNetIncome = months > 0
                ? netIncome.divide(BigDecimal.valueOf(months), RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        // 设定月均净收入系数
        BigDecimal incomeFactor = BigDecimal.ONE;
        if (averageMonthlyNetIncome.compareTo(BigDecimal.valueOf(20000)) > 0) {
            incomeFactor = BigDecimal.valueOf(1.2);
        } else if (averageMonthlyNetIncome.compareTo(BigDecimal.valueOf(15000)) > 0) {
            incomeFactor = BigDecimal.valueOf(1.1);
        } else if (averageMonthlyNetIncome.compareTo(BigDecimal.valueOf(10000)) > 0) {
            incomeFactor = BigDecimal.valueOf(1.08);
        } else if (averageMonthlyNetIncome.compareTo(BigDecimal.valueOf(5000)) > 0) {
            incomeFactor = BigDecimal.valueOf(1.05);
        } else if (averageMonthlyNetIncome.compareTo(BigDecimal.valueOf(2000)) > 0) {
            incomeFactor = BigDecimal.valueOf(1.02);
        } else if (averageMonthlyNetIncome.compareTo(BigDecimal.ZERO) > 0) {
            incomeFactor = BigDecimal.ONE;
        } else if (averageMonthlyNetIncome.compareTo(BigDecimal.valueOf(-2000)) > 0) {
            incomeFactor = BigDecimal.valueOf(0.9);
        } else if (averageMonthlyNetIncome.compareTo(BigDecimal.valueOf(-5000)) > 0) {
            incomeFactor = BigDecimal.valueOf(0.8);
        } else if (averageMonthlyNetIncome.compareTo(BigDecimal.valueOf(-10000)) > 0) {
            incomeFactor = BigDecimal.valueOf(0.7);
        } else {
            incomeFactor = BigDecimal.valueOf(0.6); // 对于非常负值
        }

        // 设定信用卡消费系数
        BigDecimal consumptionFactor = BigDecimal.ONE;
        if (totalConsumption.compareTo(BigDecimal.valueOf(50000)) > 0) {
            consumptionFactor = BigDecimal.valueOf(0.7);
        } else if (totalConsumption.compareTo(BigDecimal.valueOf(30000)) > 0) {
            consumptionFactor = BigDecimal.valueOf(0.8);
        } else if (totalConsumption.compareTo(BigDecimal.valueOf(10000)) > 0) {
            consumptionFactor = BigDecimal.valueOf(0.9);
        }

        // 设定交易流水量系数
        BigDecimal transactionFactor =         BigDecimal.ONE;
        if (transactions.size() > 50) {
            transactionFactor = BigDecimal.valueOf(1.0);
        } else if (transactions.size() > 30) {
            transactionFactor = BigDecimal.valueOf(0.95);
        } else if (transactions.size() > 20) {
            transactionFactor = BigDecimal.valueOf(0.9);
        } else if (transactions.size() > 10) {
            transactionFactor = BigDecimal.valueOf(0.85);
        } else if (transactions.size() > 5) {
            transactionFactor = BigDecimal.valueOf(0.8);
        } else {
            transactionFactor = BigDecimal.valueOf(0.7);
        }

        // 设定年龄系数
        int age = customer.getAge();
        BigDecimal ageFactor = BigDecimal.ONE;
        if (age >= 20 && age <= 35) {
            ageFactor = BigDecimal.ONE;
        } else if (age < 20 || age > 60) {
            ageFactor = BigDecimal.valueOf(0.8);
        } else if (age > 35 && age <= 45) {
            ageFactor = BigDecimal.valueOf(1.05);
        } else if (age > 45 && age <= 60) {
            ageFactor = BigDecimal.valueOf(1.1);
        } else {
            ageFactor = BigDecimal.ZERO;
        }

        // 最终信用额度计算
        creditLimit = creditLimit.multiply(incomeFactor)
                .multiply(transactionFactor)
                .multiply(ageFactor);

        return creditLimit;
    }
}

