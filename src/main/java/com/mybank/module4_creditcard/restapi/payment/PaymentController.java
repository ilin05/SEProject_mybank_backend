package com.mybank.module4_creditcard.restapi.payment;

import com.mybank.module4_creditcard.dao.impl.BillDaoImpl;
import com.mybank.module4_creditcard.entity.Bill;
import com.mybank.module4_creditcard.restapi.GeneralResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class PaymentController {

    BillDaoImpl bill_impl;

    @Autowired
    PaymentController(BillDaoImpl bill_impl) {
        this.bill_impl = bill_impl;
    }

    @PostMapping("/api/pay")
    @ResponseBody
    public GeneralResponse pay(@RequestBody Bill bill) {
        System.out.println(bill);
        boolean suc = bill_impl.payBill(bill);
        if(suc)
            return new GeneralResponse(0);
        else
            return new GeneralResponse(-1);
    }
}
