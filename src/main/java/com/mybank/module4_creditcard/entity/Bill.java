package com.mybank.module4_creditcard.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Bill {
    private String billId;
    private String customerId;
    private String creditCardId;
    private BigDecimal consumption;
    private PaymentType type;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
    private String comment;

    private String password;
    private String receiveCardId;

    public Bill() {
    }

    public Bill(String customerId, String creditCardId, BigDecimal consumption, PaymentType type, LocalDateTime time,
                String comment, String password, String receiveCardId) {
        this.customerId = customerId;
        this.creditCardId = creditCardId;
        this.consumption = consumption;
        this.type = type;
        this.time = time;
        this.comment = comment;
        this.password = password;
        this.receiveCardId = receiveCardId;
    }

    public String getBillId() {
        return billId;
    }

    public void setBillId(String billId) {
        this.billId = billId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCreditCardId() {
        return creditCardId;
    }

    public void setCreditCardId(String creditCardId) {
        this.creditCardId = creditCardId;
    }

    public BigDecimal getConsumption() {
        return consumption;
    }

    public void setConsumption(BigDecimal consumption) {
        this.consumption = consumption;
    }

    public PaymentType getType() {
        return type;
    }

    public void setType(PaymentType type) {
        this.type = type;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getPassword() {
        return password;
    }

    public String getReceiptId() {
        return receiveCardId;
    }

    // FIXME Unify internal representation with other modules
    public enum PaymentType {
        TRANSFER(0), REPAYMENT(1);

        final int val;

        PaymentType(int val) {
            this.val = val;
        }

        public static PaymentType getType(int i) {
            return switch (i) {
                case 0 -> TRANSFER;
                case 1 -> REPAYMENT;
                default -> null;
            };
        }

        public int getIntValue() {
            return val;
        }
    }
}
