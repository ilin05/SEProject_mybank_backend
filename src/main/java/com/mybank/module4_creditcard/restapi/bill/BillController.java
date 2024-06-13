package com.mybank.module4_creditcard.restapi.bill;

import com.mybank.module4_creditcard.dao.impl.BillDaoImpl;
import com.mybank.module4_creditcard.entity.Bill;
import com.mybank.module4_creditcard.restapi.GeneralResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class BillController {

    BillDaoImpl impl;

    @Autowired
    BillController(BillDaoImpl impl) { this.impl = impl; }

    @GetMapping("/api/bill/user/{user_id}")
    @ResponseBody
    public GeneralResponse getBillHistoryByUser(@PathVariable String user_id) {
        List<Bill> res = impl.queryCardBills(user_id);
        if(!res.isEmpty())
            return new GeneralResponse(res);
        else
            return new GeneralResponse(false,-1,"undetermined");
    }

    @GetMapping("/api/bill/card/{card_id}")
    @ResponseBody
    public GeneralResponse getBillHistoryByCard(@PathVariable String card_id) {
        List<Bill> res = impl.queryCardBills(card_id);
        if(!res.isEmpty())
            return new GeneralResponse(res);
        else
            return new GeneralResponse(false,-1,"undetermined");
    }
}
