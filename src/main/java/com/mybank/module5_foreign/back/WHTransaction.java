package com.mybank.module5_foreign.back;

import java.time.LocalDateTime;

public class WHTransaction {
    private int currencyFromId;
    private int currencyToId;
    private double amountFrom;
    private double amountTo;
    private LocalDateTime transactionTime;
    private String currencyFromName;
    private String currencyToName;

    // Getters and Setters
    public int getCurrencyFromId() {
        return currencyFromId;
    }

    public void setCurrencyFromId(int currencyFromId) {
        this.currencyFromId = currencyFromId;
    }

    public int getCurrencyToId() {
        return currencyToId;
    }

    public void setCurrencyToId(int currencyToId) {
        this.currencyToId = currencyToId;
    }

    public double getAmountFrom() {
        return amountFrom;
    }

    public void setAmountFrom(double amountFrom) {
        this.amountFrom = amountFrom;
    }

    public double getAmountTo() {
        return amountTo;
    }

    public void setAmountTo(double amountTo) {
        this.amountTo = amountTo;
    }

    public LocalDateTime getTransactionTime() {
        return transactionTime;
    }

    public void setTransactionTime(LocalDateTime transactionTime) {
        this.transactionTime = transactionTime;
    }

    public String getCurrencyFromName() {
        return currencyFromName;
    }

    public void setCurrencyFromName(String currencyFromName) {
        this.currencyFromName = currencyFromName;
    }

    public String getCurrencyToName() {
        return currencyToName;
    }

    public void setCurrencyToName(String currencyToName) {
        this.currencyToName = currencyToName;
    }

    @Override
    public String toString() {
        return "currency_from: " + currencyFromName + ", currency_to: " + currencyToName + ", from_amount: " + amountFrom + ", to_amount: " + amountTo + ", timestamp: " + transactionTime;
    }
}
