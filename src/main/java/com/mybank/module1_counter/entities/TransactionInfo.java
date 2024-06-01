package com.mybank.module1_counter.entities;

import java.time.LocalDateTime;

/**
 * 交易（转账）
 */
public class TransactionInfo {
    private Long transactionId;

    private Long payerId;
    private Long payeeId;
    private Double amount;
    private LocalDateTime transactionTime;

    private String transactionType;

    public TransactionInfo() {
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public Long getPayerId() {
        return payerId;
    }

    public void setPayerId(Long payerId) {
        this.payerId = payerId;
    }

    public Long getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Long payeeId) {
        this.payeeId = payeeId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public String toString() {
        return "TransactionInfo{" +
                "transactionId=" + transactionId +
                ", payerId=" + payerId +
                ", payeeId=" + payeeId +
                ", amount=" + amount +
                ", transactionTime=" + transactionTime +
                ", transactionType='" + transactionType + '\'' +
                '}';
    }
}