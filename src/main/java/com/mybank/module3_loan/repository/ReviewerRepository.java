package com.mybank.module3_loan.repository;

import com.mybank.module3_loan.model.Reviewer;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ReviewerRepository extends CrudRepository<Reviewer, Long> {
    List<Reviewer> findAll();
}
