package com.mybank.module3_loan.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Table("transaction")
public class Transaction {
    @Id
    private Long transactionId;
    private String cardId;
    private String cardType;
    private LocalDateTime transactionTime;
    private BigDecimal transactionAmount;
    private String transactionType;
    private String transactionChannel;
    private String currency;
    private String moneySource;
    private String moneyGoes;

    public Transaction(Long transactionId, String cardId, String cardType, LocalDateTime transactionTime,
                       BigDecimal transactionAmount, String transactionType, String transactionChannel,
                       String currency, String moneySource, String moneyGoes) {
        this.transactionId = transactionId;
        this.cardId = cardId;
        this.cardType = cardType;
        this.transactionTime = transactionTime;
        this.transactionAmount = transactionAmount;
        this.transactionType = transactionType;
        this.transactionChannel = transactionChannel;
        this.currency = currency;
        this.moneySource = moneySource;
        this.moneyGoes = moneyGoes;
    }

    public boolean isIncome() {
        return cardId.equals(moneyGoes);
    }

    public boolean isExpense() {
        return cardId.equals(moneySource);
    }
}
