package com.mybank.module1_counter.entities;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class FixedDepositPlan {
    Double planARatio = 0.05;
    Double planBRatio = 0.04;
    Double planCRatio = 0.03;
    Double planDRatio = 0.02;
}
