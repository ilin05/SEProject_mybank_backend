package com.mybank.module3_loan.repository;

import com.mybank.module3_loan.model.Repayment;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDate;
import java.util.List;

public interface RepaymentRepository extends CrudRepository<Repayment, Long> {
    List<Repayment> findByLoanId(Long loanApplicationId);

    List<Repayment> findByRepaymentDate(LocalDate today);
}
