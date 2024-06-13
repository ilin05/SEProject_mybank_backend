package com.mybank.module4_creditcard.restapi.application;

import com.mybank.module4_creditcard.dao.impl.ApplicationDaoImpl;
import com.mybank.module4_creditcard.entity.Application;
import com.mybank.module4_creditcard.entity.Review;
import com.mybank.module4_creditcard.restapi.GeneralResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class ApplicationController {

    ApplicationDaoImpl impl;

    @Autowired
    ApplicationController(ApplicationDaoImpl impl) { this.impl = impl; }

    @GetMapping("/api/application")
    @ResponseBody
    public GeneralResponse getApplications() {
        List<Application> res;
        try {
            res = impl.queryApplications();
        } catch (DataAccessException e) {
            return new GeneralResponse(false, 0, "cannot access data: " + e.getMessage());
        }
        return new GeneralResponse(res);
    }

    @GetMapping("/api/application/history/{user_id}")
    @ResponseBody
    public GeneralResponse getApplicationHistory(@PathVariable String user_id) {
        List<Application> res;
        try {
            res = impl.queryApplications(user_id);
        } catch (NullPointerException e) {
            return new GeneralResponse(false, 0, "null user id: " + e.getMessage());
        } catch (NumberFormatException e) {
            return new GeneralResponse(false, 1, "invalid user id: " + e.getMessage());
        } catch (DataAccessException e) {
            return new GeneralResponse(false, 2, "cannot access data. maybe wrong user id: " + e.getMessage());
        }
        return new GeneralResponse(res);
    }

    @PutMapping("/api/application/cancel/{application_id}")
    @ResponseBody
    public GeneralResponse cancelApplication(@PathVariable String application_id) {
        try {
            impl.cancelApplication(application_id);
        } catch (NullPointerException e) {
            return new GeneralResponse(false, 0, "null application id: " + e.getMessage());
        } catch (NumberFormatException e) {
            return new GeneralResponse(false, 1, "invalid application id: " + e.getMessage());
        } catch (DataAccessException e) {
            return new GeneralResponse(false, 2, "cannot access data. maybe wrong application id: " + e.getMessage());
        }
        return new GeneralResponse(0);
    }

    @PostMapping("/api/application/add")
    @ResponseBody
    public GeneralResponse addApplication(@RequestBody Application application) {
        try {
            impl.addApplication(application);
        } catch (NullPointerException e) {
            return new GeneralResponse(false, 0, "null input data: " + e.getMessage());
        } catch (NumberFormatException e) {
            return new GeneralResponse(false, 1, "invalid application format: " + e.getMessage());
        } catch (DataAccessException e) {
            return new GeneralResponse(false, 2,
                    "cannot access data. maybe wrong information provided: " + e.getMessage());
        }
        return new GeneralResponse(0);
    }

    @PutMapping("/api/application/review")
    @ResponseBody
    public GeneralResponse reviewApplication(@RequestBody Review review) {
        try {
            impl.reviewApplication(review);
        } catch (NullPointerException e) {
            return new GeneralResponse(false, 0, "null input data: " + e.getMessage());
        } catch (NumberFormatException e) {
            return new GeneralResponse(false, 1, "invalid review format: " + e.getMessage());
        } catch (DataAccessException e) {
            return new GeneralResponse(false, 2,
                    "cannot access data. maybe wrong information provided: " + e.getMessage());
        }
        return new GeneralResponse(0);
    }

}
