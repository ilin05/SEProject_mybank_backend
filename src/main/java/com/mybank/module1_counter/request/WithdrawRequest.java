package com.mybank.module1_counter.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class WithdrawRequest {
    Integer fixedDepositId;
    String accountId;
    String password;
    Double amount;
}