package com.mybank.module3_loan.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Table("loan_application")
public class LoanApplication {
    @Id
    private Long loanId;
    private BigDecimal loanAmount;
    private Long customerId;
    private String accountId;
    private String status;
    private Long reviewerId;
    private BigDecimal interestRate;
    private String loanType;
    private String repaymentStatus;
    private BigDecimal paidAmount;
    private BigDecimal totalRepaymentAmount;
    private BigDecimal collateralAmount;
    private LocalDate loanStartDate;
    private Integer loanDuration;
    private LocalDate loanEndDate;
    private String reviewComments;
    private LocalDate lastInterestUpdateDate;
    private LocalDate applicationDate;
}
