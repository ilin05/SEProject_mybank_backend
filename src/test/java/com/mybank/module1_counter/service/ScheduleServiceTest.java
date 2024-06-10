package com.mybank.module1_counter.service;

import com.mybank.module1_counter.entities.FixedDeposit;
import com.mybank.module1_counter.mapper.ScheduleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;


@SpringBootTest
class ScheduleServiceTest {
    @Autowired
    private ScheduleMapper scheduleMapper;

    @Test
    void schedule() {
        List<FixedDeposit> fixedDeposits = scheduleMapper.selectFixedDepositList();
        LocalDateTime nowTime = LocalDateTime.now();
        for (FixedDeposit fixedDeposit : fixedDeposits) {

            // 判断是否到期，未到期则 continue
            int month = fixedDeposit.getDepositDuration();
            LocalDateTime dueTime = fixedDeposit.getDepositTime().plusMonths(month);
            if (nowTime.isBefore(dueTime)) continue;

            // 到期，计算利息
            double amount = fixedDeposit.getDepositAmount();
            double interest = fixedDeposit.getDepositRate() * amount;
            String accountId = fixedDeposit.getAccountId();
            Integer fixedDepositId = fixedDeposit.getFixedDepositId();

            scheduleMapper.updateBalance(accountId, interest);
            scheduleMapper.insertTransactionForFixedInterest(accountId, interest);
            if(!fixedDeposit.getIsRenewal()) {
                scheduleMapper.deleteFixedDeposit(fixedDepositId);
            } else {
                scheduleMapper.updateFixedDepositTime(fixedDepositId);
            }
        }
    }
}