package com.mybank.module3_loan.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Table("reviewer")
public class Reviewer {
    @Id
    private Long id;
    private String name;
    private String password;
    private int approvalLevel; // 0 for small loans only, 1 for both small and large loans

}
