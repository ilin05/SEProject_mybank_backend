package com.mybank.module3_loan.repository;

import com.mybank.module3_loan.model.CreditCard;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CreditCardRepository extends CrudRepository<CreditCard, String> {
    List<CreditCard> findByCustomerId(Long customerId);
}
