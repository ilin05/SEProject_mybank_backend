package com.mybank.module3_loan.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Table("repayment")
public class Repayment {
    @Id
    private Long id;
    private Long loanId;
    private String accountId;
    private BigDecimal repaymentAmount;
    private LocalDate repaymentDate;
}
