package com.mybank.module3_loan.repository;

import com.mybank.module3_loan.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
    Customer findByIdNumber(String idNumber);
    //Customer findByCustomerId(Long customerId);
}
