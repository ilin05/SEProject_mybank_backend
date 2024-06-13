package com.mybank.module4_creditcard.entity;

import java.math.BigDecimal;

public class CreditCard {
    private String cardId;
    private String customerId;
    private String depositCardId;
    private CardStatus status;
    private BigDecimal creditLimit;
    private BigDecimal consumption;

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getDepositCardId() {
        return depositCardId;
    }

    public void setDepositCardId(String depositCardId) {
        this.depositCardId = depositCardId;
    }

    public CardStatus getStatus() {
        return status;
    }

    public void setStatus(CardStatus status) {
        this.status = status;
    }

    public BigDecimal getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(BigDecimal creditLimit) {
        this.creditLimit = creditLimit;
    }

    public BigDecimal getConsumption() {
        return consumption;
    }

    public void setConsumption(BigDecimal consumption) {
        this.consumption = consumption;
    }

    public enum CardStatus {
        ACTIVE(0), LOST(1), FROZEN(2);

        final int value;

        CardStatus(int value) {
            this.value = value;
        }

        public int getIntValue() {
            return value;
        }

        public static CardStatus getStatus(int i) {
            return switch (i) {
                case 0 -> ACTIVE;
                case 1 -> LOST;
                case 2 -> FROZEN;
                default -> throw new IllegalArgumentException("Invalid value: " + i);
            };
        }
    }
}
