package com.mybank.module3_loan.controller;

import com.mybank.module3_loan.service.NotificationService;
import com.mybank.module3_loan.service.UserService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
public class NotificationController {

    @Autowired
    private NotificationService userService;

    @PostMapping("/notification")
    public ResponseEntity<List<Long>> getUrgentLoanIds(@RequestParam Long customerId) {
        List<Long> urgentLoanIds = userService.getUrgentLoanIds(customerId);
        return ResponseEntity.ok(urgentLoanIds);
    }
}