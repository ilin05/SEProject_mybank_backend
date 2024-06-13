package com.mybank.module3_loan.controller;

import com.mybank.module3_loan.model.Repayment;
import com.mybank.module3_loan.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/notifications")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/due-repayments")
    public ResponseEntity<List<Repayment>> getDueRepayments() {
        List<Repayment> dueRepayments = notificationService.getDueRepayments();
        return ResponseEntity.ok(dueRepayments);
    }
}
