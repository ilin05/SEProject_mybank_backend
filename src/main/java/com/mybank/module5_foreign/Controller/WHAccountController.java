package com.mybank.module5_foreign.Controller;

import com.mybank.module5_foreign.back.WHUser;
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
@RequestMapping("/api/account")
public class WHAccountController {

    @Autowired
    private WHCommonFunctions commonFunctions;

    @GetMapping("/holdings")
    public ResponseEntity<?> getUserHoldings(HttpSession session) {
        try {
            WHUser WHUser = (WHUser) session.getAttribute("user");
            if (WHUser == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "WHUser not logged in"));
            }
            List<Map<String, Object>> holdings = commonFunctions.getUserHoldings(WHUser.getId());
            Double rmbBalance = commonFunctions.getUserRMBalance(WHUser.getId());
            Map<String, Object> response = new HashMap<>();
            response.put("holdings", holdings);
            response.put("rmbBalance", rmbBalance);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error fetching holdings: " + e.getMessage()));
        }
    }

    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(@RequestBody TransactionRequest request, HttpSession session) {
        WHUser WHUser = (WHUser) session.getAttribute("user");
        if (WHUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not logged in"));
        }

        try {
            boolean success = commonFunctions.toForeign(request.getAccount_id(), request.getPassword(), WHUser.getName(), request.getCurrencyId(), request.getAmount());
            if (success) {
                return ResponseEntity.ok(Map.of("message", "Deposit successful"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Deposit failed"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Internal server error"));
        }
    }

    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(@RequestBody TransactionRequest request, HttpSession session) {
        WHUser WHUser = (WHUser) session.getAttribute("user");
        if (WHUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "User not logged in"));
        }

        try {
            boolean success = commonFunctions.fromForeign(request.getAccount_id(), request.getPassword(), WHUser.getName(), request.getCurrencyId(), request.getAmount());
            if (success) {
                return ResponseEntity.ok(Map.of("message", "Withdrawal successful"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Insufficient funds or withdrawal failed"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Internal server error"));
        }
    }

    @GetMapping("/currencies")
    public ResponseEntity<?> getAllCurrencies() {
        try {
            List<Map<String, Object>> currencies = commonFunctions.getAllCurrencies();
            return ResponseEntity.ok(currencies);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", "Error fetching currencies: " + e.getMessage()));
        }
    }

    @PostMapping("/deal")
    public ResponseEntity<?> handleDeal(@RequestBody DealRequest request) {
        try {
            int result = commonFunctions.deal(request.getUsername(), request.getPaypassword(), request.getCurrency_from(), request.getCurrency_to(), request.getAmount());
            if (result == 1) {
                return ResponseEntity.ok(Map.of("success", true));
            } else if (result == -1) {
                return ResponseEntity.ok(Map.of("success", false, "message", "余额不足"));
            } else {
                return ResponseEntity.ok(Map.of("success", false, "message", "交易失败"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("success", false, "message", "服务器内部错误: " + e.getMessage()));
        }
    }

}

class TransactionRequest {
    private String account_id;
    private String password;
    private int currencyId;
    private double amount;

    // Getter 和 Setter 方法
    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(int currencyId) {
        this.currencyId = currencyId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}

class DealRequest {
    private String username;
    private String paypassword;
    private int currency_from;
    private int currency_to;
    private double amount;

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPaypassword() {
        return paypassword;
    }

    public void setPaypassword(String paypassword) {
        this.paypassword = paypassword;
    }

    public int getCurrency_from() {
        return currency_from;
    }

    public void setCurrency_from(int currency_from) {
        this.currency_from = currency_from;
    }

    public int getCurrency_to() {
        return currency_to;
    }

    public void setCurrency_to(int currency_to) {
        this.currency_to = currency_to;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}


