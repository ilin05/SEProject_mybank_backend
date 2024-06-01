package com.mybank.module1_counter.controller;

import com.mybank.module1_counter.service.CashierDutyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/cashier")
public class CashierDutyController {
    @Autowired
    private CashierDutyService cashierDutyService;

    ;
}
