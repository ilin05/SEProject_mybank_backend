package com.mybank.module1_counter.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
public class Cashier {
    private Integer cashierId;
    private String cashierName;
    private String cashierIdNumber;
    private String address;
    private String phoneNumber;
    private Character privilege;

    public Cashier() {
    }

    public Cashier(String cashierName, String cashierIdNumber, String address, String phoneNumber, Character privilege) {
        this.cashierName = cashierName;
        this.cashierIdNumber = cashierIdNumber;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.privilege = privilege;
    }

    public Cashier(Integer cashierId, String cashierName, String cashierIdNumber, String address, String phoneNumber, Character privilege) {
        this.cashierId = cashierId;
        this.cashierName = cashierName;
        this.cashierIdNumber = cashierIdNumber;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.privilege = privilege;
    }

    @Override
    public String toString() {
        return "Cashier{" +
                "cashierId=" + cashierId +
                ", cashierName='" + cashierName + '\'' +
                ", idCardNumber='" + cashierIdNumber + '\'' +
                ", address='" + address + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", privilege=" + privilege +
                '}';
    }
}
