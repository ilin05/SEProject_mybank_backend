package com.mybank.module3_loan.repository;

import com.mybank.module3_loan.model.Transaction;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> findByCardId(Long customerId);
    List<Transaction> findByCardIdIn(List<String> cardIds);
}
