package com.mybank.module1_counter.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class TransactionInfo {
    private Integer transactionId;

    private String cardId;
    private String cardType;

    private LocalDateTime transactionTime;
    private Double transactionAmount;
    private String transactionType;
    private String transactionChannel;

    private String currency;
    private String moneySource;
    private String moneyGoes;
}