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
public class SavingAccount {
    // 储蓄账户
    private String accountId;           // 储蓄账户id

    private Integer customerId;         // 顾客id
    private String customerName;        // 顾客姓名
    private String idNumber;            // 顾客身份证
    private String address;             // 顾客地址
    private String phoneNumber;         // 顾客电话号码

    private String password;            // 账户密码
    private Double balance;             // 账户余额

    private LocalDateTime openTime;     // 开户时间
    private Double openAmount;          // 开户金额（初始金额）

    private Boolean freezeState;        // 账户是否冻结
    private Boolean lossState;          // 账户是否挂失
    private Boolean deleted;            // 账户是否删除
}
