package com.mybank.module3_loan.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomerFinancialSummary {
    private BigDecimal monthlyNetIncome;
    private long monthlyTransactionCount;
    private BigDecimal totalSavings;
    private BigDecimal totalCreditCardDebt;
    private BigDecimal availableCollateral;
    private BigDecimal credit;

    public CustomerFinancialSummary(BigDecimal monthlyNetIncome, long monthlyTransactionCount, BigDecimal totalSavings, BigDecimal totalCreditCardDebt, BigDecimal availableCollateral,BigDecimal credit) {
        this.monthlyNetIncome = monthlyNetIncome;
        this.monthlyTransactionCount = monthlyTransactionCount;
        this.totalSavings = totalSavings;
        this.totalCreditCardDebt = totalCreditCardDebt;
        this.availableCollateral = availableCollateral;
        this.credit = credit;
    }
}
