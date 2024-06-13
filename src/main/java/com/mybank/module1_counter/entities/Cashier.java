package com.mybank.module1_counter.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class Cashier {
    private Integer cashierId;      // 收银员id
    private String cashierName;     // 收银员姓名
    private String idNumber;        // 收银员身份证号码
    private String phoneNumber;     // 收银员电话号码
    private String password;        // 收银员密码
    private String address;         // 收银员地址
    private Character privilege;    // 收银员权限
}
