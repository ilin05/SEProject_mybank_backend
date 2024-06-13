package com.mybank.module5_foreign.Controller;

import com.mybank.module5_foreign.back.WHOperator;
import com.mybank.module5_foreign.back.WHCommonFunctions;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/currency")
public class WHCurrencyController {

    @Autowired
    private WHCommonFunctions commonFunctions;

    private boolean isOperator(HttpSession session) {
        return session.getAttribute("operator") != null;
    }

    private boolean hasCurrencyPermission(HttpSession session) {
        WHOperator WHOperator = (WHOperator) session.getAttribute("operator");
        return WHOperator != null && WHOperator.isControlCurrency();
    }

    private boolean hasRatePermission(HttpSession session) {
        WHOperator WHOperator = (WHOperator) session.getAttribute("operator");
        return WHOperator != null && WHOperator.isControlRate();
    }

    @PostMapping("/add")
    public Map<String, Object> addCurrency(@RequestBody CurrencyRateRequest request, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        System.out.println("Received request: " + request); // 调试输出
        if (!isOperator(session) || !hasCurrencyPermission(session)) {
            response.put("success", false);
            response.put("message", "没有权限添加货币");
            return response;
        }
        try {
            int result = commonFunctions.addCurrency(request.getName(), request.getBaseExchangeRate());
            if (result == 1) {
                response.put("success", true);
            } else if (result == 0) {
                response.put("success", false);
                response.put("message", "货币已经存在");
            } else {
                response.put("success", false);
                response.put("message", "非法输入");
            }
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常日志
            response.put("success", false);
            response.put("message", "服务器内部错误: " + e.getMessage());
        }
        return response;
    }

    @PostMapping("/delete")
    public Map<String, Object> deleteCurrency(@RequestBody CurrencyRateRequest request, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        if (!isOperator(session) || !hasCurrencyPermission(session)) {
            response.put("success", false);
            response.put("message", "没有权限删除货币");
            return response;
        }
        boolean result = commonFunctions.deleteCurrency(request.getName());
        response.put("success", result);
        if (!result) {
            response.put("message", "删除失败，货币不存在或无法删除");
        }
        return response;
    }

    @PostMapping("/modify")
    public Map<String, Object> modifyRate(@RequestBody CurrencyRateRequest request, HttpSession session) {
        Map<String, Object> response = new HashMap<>();
        if (!isOperator(session) || !hasRatePermission(session)) {
            response.put("success", false);
            response.put("message", "没有权限修改汇率");
            return response;
        }
        try {
            boolean result = commonFunctions.changeRate(request.getId(), request.getNewRate());
            response.put("success", result);
            if (!result) {
                response.put("message", "修改汇率失败，货币不存在或输入汇率有问题");
            }
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常日志
            response.put("success", false);
            response.put("message", "服务器内部错误: " + e.getMessage());
        }
        return response;
    }

    @GetMapping("/list")
    public Map<String, Object> listCurrencies() {
        List<Map<String, Object>> currencies = commonFunctions.getAllCurrencies();
        Map<String, Object> response = new HashMap<>();
        response.put("currencies", currencies);
        return response;
    }

    @GetMapping("/exchangeRate")
    public ResponseEntity<?> getExchangeRate(@RequestParam int currencyFromId, @RequestParam int currencyToId) {
        try {
            double rate = commonFunctions.getExchangeRate(currencyFromId, currencyToId);
            return ResponseEntity.ok(Map.of("rate", rate));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error fetching exchange rate: " + e.getMessage()));
        }
    }
}

class CurrencyRateRequest {
    private String name;
    private double baseExchangeRate;
    private double newRate;
    private int id;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getBaseExchangeRate() { return baseExchangeRate; }
    public void setBaseExchangeRate(double baseExchangeRate) { this.baseExchangeRate = baseExchangeRate; }

    public double getNewRate() { return newRate; }
    public void setNewRate(double newRate) { this.newRate = newRate; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
}
