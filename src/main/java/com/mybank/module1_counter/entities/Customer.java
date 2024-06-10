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
    private Integer customerId;
    private String customerName;
    private String idNumber;
    private String phoneNumber;
    private String address;
    private Double creditLine;
    private Double assets;
    private Boolean internetBankOpen;
}
