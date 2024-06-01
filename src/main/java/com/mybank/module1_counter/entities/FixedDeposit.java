package com.mybank.module1_counter.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FixedDeposit {

    private Long fixedDepositId;
    private Long accountId;
    private Double amount;
    private LocalDateTime depositTime;

    private Double ratio;
    private Integer period;
    private Boolean isRenewal;

    public FixedDeposit() {
    }

    @Override
    public String toString() {
        return "FixedDeposit{" +
                "fixedDepositId=" + fixedDepositId +
                ", accountId=" + accountId +
                ", amount=" + amount +
                ", depositTime=" + depositTime +
                ", ratio=" + ratio +
                ", period=" + period +
                ", isRenewal=" + isRenewal +
                '}';
    }
}
