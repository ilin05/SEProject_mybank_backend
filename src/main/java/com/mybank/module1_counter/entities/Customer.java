package com.mybank.module1_counter.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class Customer {
    private Integer customerId;         // 顾客id
    private String customerName;        // 顾客姓名
    private String idNumber;            // 顾客身份证
    private String phoneNumber;         // 顾客电话号码
    private String address;             // 顾客地址
    private Double creditLine;          // 顾客信用额度（顾客在银行透支的最大金额）（PS:这个应该是贷款模块提供的）
    private Double assets;              // 顾客资产（存款？）
    private Boolean internetBankOpen;   // 该顾客是否已开通网银服务（在柜台系统开启）
}
