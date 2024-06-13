package com.mybank.module3_loan.controller;

//import com.mybank.module3_loan.model.Admin;
import com.mybank.module3_loan.model.Reviewer;
import com.mybank.module3_loan.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS})
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

//    @PostMapping("/login")
//    public ResponseEntity<Admin> login(@RequestParam String username, @RequestParam String password) {
//        Admin admin = adminService.login(username, password);
//        if (admin != null) {
//            return ResponseEntity.ok(admin);
//        }
//        return ResponseEntity.status(401).build();
//    }

    @PostMapping("/reviewers/create")
    public ResponseEntity<Reviewer> createReviewer(@RequestBody Reviewer reviewer) {
        Reviewer createdReviewer = adminService.createReviewer(reviewer);
        return ResponseEntity.ok(createdReviewer);
    }

    @PutMapping("/reviewers/update")
    public ResponseEntity<Reviewer> updateReviewer(@RequestBody Reviewer reviewer) {
        Reviewer updatedReviewer = adminService.updateReviewer(reviewer);
        return ResponseEntity.ok(updatedReviewer);
    }

    @DeleteMapping("/reviewers/delete/{reviewerId}")
    public ResponseEntity<Void> deleteReviewer(@PathVariable Long reviewerId) {
        adminService.deleteReviewer(reviewerId);
        return ResponseEntity.ok().build();
    }
}
