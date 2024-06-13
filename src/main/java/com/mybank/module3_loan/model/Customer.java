package com.mybank.module3_loan.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Table("customer")
public class Customer {
    @Id
    private Long customerId;
    private String customerName;
    private String idNumber;
    private String phoneNumber;
    private String address;
    private Double creditLine;
    private BigDecimal assets;
    private Boolean internetBankOpen;

    // 解析年龄的方法
    public int getAge() {
        if (idNumber == null || idNumber.length() < 14) {
            throw new IllegalArgumentException("Invalid ID number");
        }
        String birthDate = idNumber.substring(6, 14); // 取出身份证号中的出生日期
        int birthYear = Integer.parseInt(birthDate.substring(0, 4));
        int birthMonth = Integer.parseInt(birthDate.substring(4, 6));
        int birthDay = Integer.parseInt(birthDate.substring(6, 8));

        java.time.LocalDate birthLocalDate = java.time.LocalDate.of(birthYear, birthMonth, birthDay);
        java.time.Period age = java.time.Period.between(birthLocalDate, java.time.LocalDate.now());

        return age.getYears();
    }

}
