package com.mybank.module1_counter.entities;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
public class SavingAccount {
    private Long accountId;

    private Long customerId;
    private String customerName;
    private String customerIdNumber;
    private String customerAddress;
    private String customerPhoneNumber;

    private String password;
    private Double balance;
    private LocalDateTime openTime;
    private Double openAmount;

    private Boolean AccountState;

    public SavingAccount() {
    }

    @Override
    public String toString() {
        return "SavingAccount{" +
                "accountId=" + accountId +
                ", customerId=" + customerId +
                ", customerName='" + customerName + '\'' +
                ", customerIdNumber='" + customerIdNumber + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", customerPhoneNumber='" + customerPhoneNumber + '\'' +
                ", password='" + password + '\'' +
                ", balance=" + balance +
                ", openTime=" + openTime +
                ", openAmount=" + openAmount +
                ", AccountState=" + AccountState +
                '}';
    }
}
