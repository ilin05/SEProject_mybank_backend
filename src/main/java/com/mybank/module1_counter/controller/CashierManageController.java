package com.mybank.module1_counter.controller;

import com.mybank.module1_counter.service.CashierManageService;
import com.mybank.module1_counter.entities.Cashier;
import com.mybank.module2_counter.entities.Internet;
import com.mybank.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE,RequestMethod.OPTIONS})
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
    @DeleteMapping("/cashier")
    public ApiResult deleteCashier(@RequestParam int cashierId){
        return cashierManageService.removeCashier(cashierId);
    }

    @GetMapping("/internet")
    public ApiResult getAllInternet() {
        return cashierManageService.getAllInternet();
    }

    @PostMapping("/internet/unblock")
    public ApiResult unblockInternetAccount(@RequestBody Map<String, String> Request){
        String internetId = Request.get("internetId");
        //System.out.println(internetId);
        return cashierManageService.unblockInternet(internetId);
    }

    @PostMapping("/internet/block")
    public ApiResult blockInternetAccount(@RequestBody Map<String, String> Request){
        String internetId = Request.get("internetId");
        System.out.println(internetId);
        return cashierManageService.blockInternet(internetId);
    }
}
