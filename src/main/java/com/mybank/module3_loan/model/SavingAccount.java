package com.mybank.module3_loan.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Data
@Table("saving_account")
public class SavingAccount {
    @Id
    private String accountId;
    private Long customerId;
    private String password;
    private Double balance;
    private Boolean freezeState;
    private Boolean lossState;
    private Boolean deleted;
    private LocalDateTime openTime;
    private Double openAmount;
    private Double interest;
}
