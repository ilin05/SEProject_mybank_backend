package com.mybank.module3_loan.service;

//import com.mybank.module3_loan.model.Admin;
import com.mybank.module3_loan.model.Reviewer;
//import com.mybank.module3_loan.repository.AdminRepository;
import com.mybank.module3_loan.repository.ReviewerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

//    @Autowired
//    private AdminRepository adminRepository;

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

    public Reviewer createReviewer(Reviewer reviewer) {
        reviewer.setPassword(passwordEncoder.encode(reviewer.getPassword()));
        return reviewerRepository.save(reviewer);
    }

    public Reviewer updateReviewer(Reviewer reviewer) {
        reviewer.setPassword(passwordEncoder.encode(reviewer.getPassword()));
        return reviewerRepository.save(reviewer);
    }

    public void deleteReviewer(Long reviewerId) {
        reviewerRepository.deleteById(reviewerId);
    }
}
