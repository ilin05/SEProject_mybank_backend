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
    private Integer customerId;         //用户的ID，这里的ID并不是身份证号，而是在数据库中存储时的序号，
                                        //而数据库中已经设置自增了，所以其实大部分时候用不到
    private String customerName;
    private String idNumber;            //身份证号
    private String phoneNumber;
    private String address;
    private Double creditLine;
    private Double assets;
    private Boolean internetBankOpen;
}
