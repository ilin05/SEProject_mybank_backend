package com.mybank.module3_loan.service;

import com.mybank.module3_loan.model.Reviewer;
import com.mybank.module3_loan.repository.ReviewerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {


    @Autowired
    private ReviewerRepository reviewerRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

//    public Admin login(String username, String password) {
//        Admin admin = adminRepository.findByUsername(username);
//        if (admin != null && passwordEncoder.matches(password, admin.getPassword())) {
//            return admin;
//        }
//        return null;
//    }

    public Reviewer createReviewer(String username, String password, Integer approvalLevel) {
        Reviewer reviewer = new Reviewer();
        reviewer.setPassword(password);
        reviewer.setName(username);
        reviewer.setApprovalLevel(approvalLevel);
        return reviewerRepository.save(reviewer);
    }

    public Reviewer updateReviewer(Long reviewerId, String username, String password, Integer approvalLevel) {
        Reviewer reviewer = reviewerRepository.findById(reviewerId).orElseThrow();
        reviewer.setPassword(password);
        reviewer.setName(username);
        reviewer.setApprovalLevel(approvalLevel);
        return reviewerRepository.save(reviewer);
    }

    public void deleteReviewer(Long reviewerId) {
        reviewerRepository.deleteById(reviewerId);
    }

    public List<Reviewer> findReviewer() {
        return reviewerRepository.findAll();
    }
}
