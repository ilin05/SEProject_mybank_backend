package com.mybank.module5_foreign.Controller;

import com.mybank.module5_foreign.back.WHUser;
import com.mybank.module5_foreign.back.WHCommonFunctions;
import com.mybank.module5_foreign.back.WHTransaction;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/transaction")
public class WHTransactionController {

    @Autowired
    private WHCommonFunctions commonFunctions;

    @GetMapping("/history")
    public List<WHTransaction> getTransactionHistory(HttpSession session, @RequestParam(defaultValue = "40") int threshold) {
        WHUser WHUser = (WHUser) session.getAttribute("user");
        if (WHUser == null) {
            throw new RuntimeException("用户未登录");
        }
        return commonFunctions.getAllBalanceByUser(WHUser.getName(), threshold);
    }
}
