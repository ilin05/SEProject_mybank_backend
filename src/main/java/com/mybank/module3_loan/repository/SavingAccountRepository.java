package com.mybank.module3_loan.repository;

import com.mybank.module3_loan.model.SavingAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingAccountRepository extends CrudRepository<SavingAccount, String> {
    List<SavingAccount> findByCustomerId(Long customerId);
    List<SavingAccount> findByCustomerIdAndBalanceGreaterThan(Long customerId, double balance);
}
