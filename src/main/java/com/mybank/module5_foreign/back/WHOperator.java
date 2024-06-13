package com.mybank.module5_foreign.back;

public class WHOperator {
    private int optId;
    private String optName;
    private String optPassword;
    private boolean controlCurrency;
    private boolean controlRate;

    // Getters and Setters

    public int getOptId() {
        return optId;
    }

    public void setOptId(int optId) {
        this.optId = optId;
    }

    public String getOptName() {
        return optName;
    }

    public void setOptName(String optName) {
        this.optName = optName;
    }

    public String getOptPassword() {
        return optPassword;
    }

    public void setOptPassword(String optPassword) {
        this.optPassword = optPassword;
    }

    public boolean isControlCurrency() {
        return controlCurrency;
    }

    public void setControlCurrency(boolean controlCurrency) {
        this.controlCurrency = controlCurrency;
    }

    public boolean isControlRate() {
        return controlRate;
    }

    public void setControlRate(boolean controlRate) {
        this.controlRate = controlRate;
    }
}

