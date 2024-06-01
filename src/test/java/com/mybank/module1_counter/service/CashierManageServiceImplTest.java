package com.mybank.module1_counter.service;

import com.mybank.module1_counter.entities.Cashier;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CashierManageServiceImplTest {
    @Autowired
    private CashierManageServiceImpl cashierManageService;

    @Test
    public void testAddCashier() {
        Cashier cashier = new Cashier();
        cashier.setCashierName("czt");
        cashier.setIdNumber("440514");
        cashier.setPhoneNumber("17336083845");
        cashier.setPassword("123456");
        cashier.setAddress("guangdong");
        cashier.setPrivilege('A');
        cashierManageService.addCashier(cashier);
    }
}