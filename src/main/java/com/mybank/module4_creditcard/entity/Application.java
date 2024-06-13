package com.mybank.module4_creditcard.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Application {
    private String applId;
    private String customerId;
    private String cardId;
    private String depositId;
    private BigDecimal limit;
    private String password;
    private String comment;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime time;
    private ApplStatus status;

    public Application() {
    }

    public Application(String customerId, String depositId, BigDecimal limit, String password, String comment,
                       LocalDateTime time) {
        this.customerId = customerId;
        this.depositId = depositId;
        this.limit = limit;
        this.password = password;
        this.comment = comment;
        this.time = time;
    }

    public LocalDateTime getTime() {
        return time;
    }

    public void setTime(LocalDateTime time) {
        this.time = time;
    }

    public String getApplId() {
        return applId;
    }

    public void setApplId(String applId) {
        this.applId = applId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCardId() { return cardId; }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getDepositId() {
        return depositId;
    }

    public void setDepositId(String depositId) {
        this.depositId = depositId;
    }

    public BigDecimal getLimit() {
        return limit;
    }

    public void setLimit(BigDecimal limit) {
        this.limit = limit;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ApplStatus getStatus() {
        return status;
    }

    public void setStatus(ApplStatus status) {
        this.status = status;
    }

    public enum ApplStatus {
        REVIEWING(0), CANCELED(1), APPROVED(2), REJECTED(3);

        final int val;

        ApplStatus(int val) {
            this.val = val;
        }

        public static ApplStatus getStatus(int i) {
            return switch (i) {
                case 0 -> REVIEWING;
                case 1 -> CANCELED;
                case 2 -> APPROVED;
                case 3 -> REJECTED;
                default -> null;
            };
        }

        public int getIntValue() {
            return val;
        }
    }

}
