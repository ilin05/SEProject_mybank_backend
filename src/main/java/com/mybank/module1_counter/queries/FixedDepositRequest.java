package com.mybank.module1_counter.queries;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class FixedDepositRequest {
    private String accountId;

    private String password;

    private String depositType;

    private Double amount;

    public FixedDepositRequest(){
        this.accountId = null;
        this.password = null;
        this.depositType = null;
        this.amount = null;
    }

}
