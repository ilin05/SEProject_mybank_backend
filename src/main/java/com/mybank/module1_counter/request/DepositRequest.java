package com.mybank.module1_counter.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DepositRequest {
    String accountId;
    String password;
    String depositType;
    Double amount;
    Boolean isRenewal;
}
