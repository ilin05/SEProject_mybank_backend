package com.mybank.cashier_manage;

import com.mybank.module1_counter.service.CashierService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CashierServiceImplTest {

    @Autowired
    private CashierService cashierService;

    @Test
    public void sss() {
        System.out.println(cashierService.getCashier());
    }
}