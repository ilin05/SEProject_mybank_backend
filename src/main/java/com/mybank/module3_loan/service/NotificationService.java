package com.mybank.module3_loan.service;

import com.mybank.module3_loan.model.Repayment;
import com.mybank.module3_loan.repository.RepaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationService {

    @Autowired
    private RepaymentRepository repaymentRepository;

    public List<Repayment> getDueRepayments() {
        LocalDate today = LocalDate.now();
        return repaymentRepository.findByRepaymentDate(today);
    }
}
