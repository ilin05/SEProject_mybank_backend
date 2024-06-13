package com.mybank.module3_loan.repository;

import com.mybank.module3_loan.model.LoanApplication;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface LoanApplicationRepository extends CrudRepository<LoanApplication, Long> {
    List<LoanApplication> findByCustomerId(Long customerId);
    List<LoanApplication> findAll();
    List<LoanApplication> findByCustomerIdAndStatus(Long customerId, String status);
    List<LoanApplication> findByCustomerIdAndRepaymentStatusIn(Long customerId, List<String> repaymentStatuses);
    List<LoanApplication> findByCustomerIdAndStatusAndRepaymentStatus(Long customerId, String status, String repaymentStatus);
    Optional<LoanApplication> findById(Long loanApplicationId); // 新增方法
}
