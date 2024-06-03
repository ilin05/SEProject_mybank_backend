package com.mybank.module1_counter.queries;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class TransferRequest {
    private String cardId;
    private String password;
    private Double transactionAmount;
    private String moneyGoes;
}
