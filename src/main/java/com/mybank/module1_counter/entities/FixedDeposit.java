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
public class FixedDeposit {

    private Integer fixedDepositId;
    private String accountId;
    private LocalDateTime depositTime;
    private Double depositAmount;

    private Boolean isRenewal;

    // deposit type ??
    private String depositType;
    private Integer depositDuration;
    private Double depositRate;
}
