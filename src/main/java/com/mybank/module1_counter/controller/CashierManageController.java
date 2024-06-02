package com.mybank.module1_counter.controller;

import com.mybank.module1_counter.service.CashierManageService;
import com.mybank.module1_counter.entities.Cashier;
import com.mybank.utils.ApiResult;
import com.mybank.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/admin")
public class CashierManageController {

    private static final String adminUsername = "123456";
    private static final String adminPassword = "123456";

    @Autowired
    private CashierManageService cashierManageService;

    @PostMapping("/login")
    public ApiResult login(@RequestBody Map<String,String> loginRequest){
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        if(username.equals(adminUsername) && password.equals(adminPassword)){
            Map<String,Object> claims = new HashMap<>();
            claims.put("username",username);
            claims.put("password",password);
            String jwt = JwtUtils.generateJwt(claims);
            return ApiResult.success(jwt);
        }
        else return ApiResult.failure("username/password incorrect");
    }

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
