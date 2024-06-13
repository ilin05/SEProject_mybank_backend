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
    // 定期存款
    private Integer fixedDepositId;         // 定期存款id
    private String accountId;               // 存款的卡id
    private LocalDateTime depositTime;      // 存款时间
    private Double depositAmount;           // 存款金额

    private Boolean isRenewal;              // 该存款是否续存（https://zhidao.baidu.com/question/315705278276184484.html#:~:text=%E9%93%B6%E8%A1%8C%E5%AD%98%E6%AC%BE%E7%BB%AD%E5%AD%98%E6%98%AF%E6%8C%87,%E7%9A%84%E6%97%A5%E5%88%A9%E7%8E%87%E6%9D%A5%E8%AE%A1%E7%AE%97%E3%80%82）

    // deposit type ??
    private String depositType;             // 存款类型
    private Integer depositDuration;        // 存款期限
    private Double depositRate;             // 存款利率
}
