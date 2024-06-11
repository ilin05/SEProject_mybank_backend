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

    private Integer fixedDepositId;     //这个也是数据库里的ID，存记录的时候不必要设置，取得时候可能要用到
    private String accountId;
    private LocalDateTime depositTime;  //存款的时间
    private Double depositAmount;       //存款金额

    private Boolean isRenewal;          //定期存款是否续期

    // deposit type ??
    private String depositType;         //定期存款类型，不同套餐的时间和利率不同
    private Integer depositDuration;    //定期存款的时间，其实可以根据类型获取
    private Double depositRate;         //定期存款的利率，其实也可以根据类型从数据库中获取
}
