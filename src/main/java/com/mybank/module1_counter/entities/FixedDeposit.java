package com.mybank.module1_counter.entities;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class FixedDeposit {

    private Integer fixedDepositId;
    private String accountId;
    private LocalDateTime depositTime;
    private Double depositAmount;

    // deposit type ??
    private Double ratio;
    private Integer period;
    private Boolean isRenewal;

    public FixedDeposit() {
    }
}
