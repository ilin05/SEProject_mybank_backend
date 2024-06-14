package com.mybank.module3_loan.controller;

import com.mybank.module3_loan.model.Admin;
import com.mybank.module3_loan.model.Reviewer;
import com.mybank.module3_loan.service.AdminService;
import com.mybank.module3_loan.mapper.LoanMapper;
import com.mybank.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private LoanMapper loanMapper;


    @PostMapping("/examiner/add")
    public ResponseEntity<String> createReviewer(@RequestParam String name,
                                                   @RequestParam String password,
                                                   @RequestParam Integer approvalLevel) {
        System.out.println(name);
        System.out.println(password);
        System.out.println(approvalLevel);
        Reviewer createdReviewer;
        createdReviewer = adminService.createReviewer(name, password, approvalLevel);
        return ResponseEntity.ok("success");
    }

    @GetMapping("/examiner/list")
    public ResponseEntity<List<Reviewer>> findReviewer() {
        List<Reviewer> reviewers =  adminService.findReviewer();
        return ResponseEntity.ok(reviewers);
    }

    @PostMapping("/examiner/modify")
    public ResponseEntity<String> updateReviewer(@RequestParam Long id,
                                                   @RequestParam String password,
                                                   @RequestParam Integer approvalLevel,
                                                   @RequestParam String name) {
        Reviewer updatedReviewer = adminService.updateReviewer(id, name, password, approvalLevel);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/examiner/delete")
    public ResponseEntity<String> deleteReviewer(@RequestParam Long id) {
        adminService.deleteReviewer(id);
        return ResponseEntity.ok("success");
    }

    @PostMapping("/loan/login")
    public ApiResult LoanLogin(@RequestBody Map<String, String> Request){
        int loanId = Integer.parseInt(Request.get("loanId"));
        String password = Request.get("password");
        System.out.println(loanId);
        System.out.println(password);
        int result = loanMapper.judgePassword(loanId, password);
        if(result == 0){
            return ApiResult.failure("900");
        }else{
            return ApiResult.success("1");
        }
    }
}
