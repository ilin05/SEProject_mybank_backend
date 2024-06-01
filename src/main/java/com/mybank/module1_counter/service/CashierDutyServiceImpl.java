package com.mybank.module1_counter.service;

import com.mybank.module1_counter.mapper.CashierDutyMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CashierDutyServiceImpl implements CashierDutyService {
    @Autowired
    private CashierDutyMapper cashierDutyMapper;

}
