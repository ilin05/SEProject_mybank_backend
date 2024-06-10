package com.mybank.module1_counter.controller;

import com.mybank.module1_counter.service.CashierManageService;
import com.mybank.module1_counter.entities.Cashier;
import com.mybank.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/admin")
public class CashierManageController {

    @Autowired
    private CashierManageService cashierManageService;

    // select
    @GetMapping("/cashier")
    public ApiResult getAllCashier() {
        return cashierManageService.getCashier();
    }

    // insert
    @PostMapping("/cashier")
    public ApiResult addCashier(@RequestBody Cashier cashier){
        return cashierManageService.addCashier(cashier);
    }

    // update
    @PutMapping("/cashier")
    public ApiResult updateCashier(@RequestBody Cashier cashier){
        return cashierManageService.modifyCashier(cashier);
    }

    // delete
    @DeleteMapping("/cashier/{cashierId}")
    public ApiResult deleteCashier(@PathVariable int cashierId){
        return cashierManageService.removeCashier(cashierId);
    }
}
