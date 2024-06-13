package com.mybank.module3_loan.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Data
@Table("creditcards")
public class CreditCard {
    @Id
    private String cardId;
    private Long customerId;
    private String depositCardId;
    private String password;
    private int status;
    private BigDecimal creditLimit;
    private BigDecimal consumption;
}
