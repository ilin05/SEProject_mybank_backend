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
    private String accountId;

    private Integer customerId;
    private String customerName;
    private String idNumber;
    private String address;
    private String phoneNumber;

    private String password;
    private Double balance;

    private LocalDateTime openTime;
    private Double openAmount;

    private Boolean state;
}
