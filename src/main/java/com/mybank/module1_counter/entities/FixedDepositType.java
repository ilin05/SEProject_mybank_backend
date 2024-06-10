package com.mybank.module1_counter.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class FixedDepositType {
    private String depositType;
    private Integer depositDuration;
    private Double depositRate;
}
