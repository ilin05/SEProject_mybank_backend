package com.mybank.module1_counter.controller;

import com.mybank.module1_counter.entities.Cashier;
import com.mybank.module1_counter.mapper.CashierManageMapper;
import com.mybank.utils.ApiResult;
import com.mybank.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
public class LoginController {

    private static final String ADMIN_USERNAME = "123456";
    private static final String ADMIN_PASSWORD = "123456";

    @PostMapping("/admin/login")
    public ApiResult adminLogin(@RequestBody Map<String,String> loginRequest){
        String username = loginRequest.get("adminUsername");
        String password = loginRequest.get("adminPassword");
        if(!ADMIN_USERNAME.equals(username) || !ADMIN_PASSWORD.equals(password)){
            return ApiResult.failure("username/password incorrect");
        }
        Map<String,Object> claims = new HashMap<>();
        claims.put("role","admin");
        claims.put("username",username);
        String jwt = JwtUtils.generateJwt(claims);
        return ApiResult.success(jwt);
    }


    @Autowired
    private CashierManageMapper cashierManageMapper;

    @PostMapping("/cashier/login")
    public ApiResult cashierLogin(@RequestBody Map<String,Object> loginRequest){
        int cashierId = (int) loginRequest.get("cashierId");
        String password = (String) loginRequest.get("password");
        Cashier cashier = cashierManageMapper.getOneCashier(cashierId, password);
        if(cashier == null){
            return ApiResult.failure("cashierId/password incorrect");
        }
        Map<String,Object> claims = new HashMap<>();
        claims.put("role","cashier");
        claims.put("privilege",cashier.getPrivilege());
        claims.put("cashierId",cashierId);
        claims.put("cashierName",cashier.getCashierName());
        String jwt = JwtUtils.generateJwt(claims);
        return ApiResult.success(jwt);
    }
}
