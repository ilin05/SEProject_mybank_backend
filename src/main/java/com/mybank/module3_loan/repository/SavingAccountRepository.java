package com.mybank.module3_loan.repository;

import com.mybank.module3_loan.model.SavingAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavingAccountRepository extends CrudRepository<SavingAccount, String> {
    List<SavingAccount> findByCustomerId(Long customerId);
    List<SavingAccount> findByCustomerIdAndBalanceGreaterThan(Long customerId, double balance);

    // New method to find accounts that are not deleted
    List<SavingAccount> findByDeletedFalse();

    // You can also combine this with other criteria, for example:
    List<SavingAccount> findByCustomerIdAndDeletedFalse(Long customerId);
    List<SavingAccount> findByCustomerIdAndBalanceGreaterThanAndDeletedFalse(Long customerId, double balance);

}

