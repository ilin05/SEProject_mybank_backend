package com.mybank.module1_counter.controller;

import com.mybank.module1_counter.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class TransactionController {

    @Autowired
    private TransactionService transactionService;
}
