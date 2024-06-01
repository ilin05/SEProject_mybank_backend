package com.mybank.module1_counter.controller;

import com.mybank.module1_counter.service.StateRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
public class StateRecordController {
    @Autowired
    private StateRecordService stateRecordService;

}
