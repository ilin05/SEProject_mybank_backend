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
    private Integer cashierId;
    private String cashierName;
    private String idNumber;
    private String phoneNumber;
    private String password;
    private String address;
    private Character privilege;
}
