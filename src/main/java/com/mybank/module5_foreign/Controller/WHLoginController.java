package com.mybank.module5_foreign.Controller;

import com.mybank.module5_foreign.back.WHOperator;
import com.mybank.module5_foreign.back.WHUser;
import com.mybank.module5_foreign.back.WHCommonFunctions;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/login")
public class WHLoginController {

    @Autowired
    private WHCommonFunctions commonFunctions;

    @PostMapping
    public Map<String, Object> login(@RequestBody UserLoginRequest request, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        try {
            int userType = commonFunctions.findUser(request.getName(), request.getPassword());
            response.put("userType", userType);
            if (userType == 1) { // 普通用户
                WHUser WHUser = commonFunctions.findUserDetails(request.getName(), request.getPassword());
                if (WHUser != null) {
                    session.setAttribute("user", WHUser);
                } else {
                    response.put("success", false);
                    response.put("message", "用户信息错误");
                    return response;
                }
            }
            else if (userType == 2 || userType == 3) {  // 操作员或管理员
                WHOperator WHOperator;
                if (userType == 3) {  // 管理员
                    WHOperator = new WHOperator();
                    WHOperator.setControlCurrency(true);
                    WHOperator.setControlRate(true);
                } else {
                    WHOperator = commonFunctions.findOperator(request.getName(), request.getPassword());
                    if (WHOperator == null) {
                        response.put("success", false);
                        response.put("message", "操作员信息错误");
                        return response;
                    }
                }
                session.setAttribute("operator", WHOperator);
            }
            response.put("success", true);
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常日志
            response.put("success", false);
            response.put("message", "服务器内部错误: " + e.getMessage());
        }
        return response;
    }
}

class UserLoginRequest {
    private String name;
    private String password;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
