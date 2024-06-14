package com.mybank.module4_creditcard.restapi.auditor;


import com.mybank.module4_creditcard.mapper.AuditorMapper;
import com.mybank.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin
public class AuditorController {
    @Autowired
    private AuditorMapper auditorMapper;

    @PostMapping("/auditor/login")
    public ApiResult AuditorLogin(@RequestBody Map<String, String> loginRequest) {
        //System.out.println("hello250");
        System.out.println(loginRequest);
        int auditorId = Integer.valueOf(loginRequest.get("auditorId"));
        String password = loginRequest.get("password");
        System.out.println(password);
        System.out.println(auditorId);
        int count = auditorMapper.checkPassword(auditorId, password);
        System.out.println(count);
        if(count > 0) {
            return ApiResult.success('1');
        }else{
            return ApiResult.failure("password error!");
        }
    }
}
