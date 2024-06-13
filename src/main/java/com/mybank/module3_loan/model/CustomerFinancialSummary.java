package com.mybank.module3_loan.model;

import java.math.BigDecimal;

public class CustomerFinancialSummary {
    private BigDecimal monthlyNetIncome;
    private long monthlyTransactionCount;
    private BigDecimal totalSavings;
    private BigDecimal totalCreditCardDebt;
    private BigDecimal availableCollateral;

    public CustomerFinancialSummary(BigDecimal monthlyNetIncome, long monthlyTransactionCount, BigDecimal totalSavings, BigDecimal totalCreditCardDebt, BigDecimal availableCollateral) {
        this.monthlyNetIncome = monthlyNetIncome;
        this.monthlyTransactionCount = monthlyTransactionCount;
        this.totalSavings = totalSavings;
        this.totalCreditCardDebt = totalCreditCardDebt;
        this.availableCollateral = availableCollateral;
    }

    // Getters and setters...

    public BigDecimal getMonthlyNetIncome() {
        return monthlyNetIncome;
    }

    public void setMonthlyNetIncome(BigDecimal monthlyNetIncome) {
        this.monthlyNetIncome = monthlyNetIncome;
    }

    public long getMonthlyTransactionCount() {
        return monthlyTransactionCount;
    }

    public void setMonthlyTransactionCount(long monthlyTransactionCount) {
        this.monthlyTransactionCount = monthlyTransactionCount;
    }

    public BigDecimal getTotalSavings() {
        return totalSavings;
    }

    public void setTotalSavings(BigDecimal totalSavings) {
        this.totalSavings = totalSavings;
    }

    public BigDecimal getTotalCreditCardDebt() {
        return totalCreditCardDebt;
    }

    public void setTotalCreditCardDebt(BigDecimal totalCreditCardDebt) {
        this.totalCreditCardDebt = totalCreditCardDebt;
    }

    public BigDecimal getAvailableCollateral() {
        return availableCollateral;
    }

    public void setAvailableCollateral(BigDecimal availableCollateral) {
        this.availableCollateral = availableCollateral;
    }
}
