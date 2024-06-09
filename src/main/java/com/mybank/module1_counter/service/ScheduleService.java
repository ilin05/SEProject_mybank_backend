package com.mybank.module1_counter.service;

import com.mybank.module1_counter.entities.FixedDeposit;
import com.mybank.module1_counter.mapper.ScheduleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.time.LocalDateTime;
import java.util.List;

@Component
public class ScheduleService {
    // 活期年利率
    private static final double demandDepositYearRate = 0.0035;

    @Autowired
    private ScheduleMapper scheduleMapper;

    // 更新一次利息列（一天一次、凌晨0点执行）
    @Scheduled(cron = "0 0 0 * * ?")
    public void updateInterest() {
        try {
            scheduleMapper.updateInterest(demandDepositYearRate/365);
        } catch (Exception e) {
            System.out.println("Error updating interest");
        }
    }

    // 更新一次余额列、即结息（三个月一次，1.1,、4.1、7.1、10.1）
    @Scheduled(cron = "0 0 0 1 1,4,7,10 ?")
    @Transactional
    public void updateBalance() {
        try {
            scheduleMapper.insertTransactionForDemandInterest();
            scheduleMapper.updateAllBalance();
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            System.out.println("Error in interest settlement");
        }
    }

    // 到期解冻（凌晨0点执行）
    @Scheduled(cron = "0 0 0 * * ?")
    public void autoUnfreeze() {
        try {
            List<String> frozenAccountIds = scheduleMapper.selectFrozenAccountList();
            for (String accountId : frozenAccountIds) {
                Integer freezeRecordId = scheduleMapper.selectFreezeRecord(accountId);
                int ok = scheduleMapper.updateUnfreezeTime(freezeRecordId);
                if (ok > 0) scheduleMapper.updateFreezeRecord(accountId);
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            System.out.println("Error in autoUnfreeze");
        }
    }

    // 定期到期（凌晨0点执行）
    @Scheduled(cron = "0 0 0 * * ?")
    public void fixedDepositInterestSettle() {
        try {
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

                if(fixedDeposit.getIsRenewal()) { // 续期
                    scheduleMapper.updateBalance(accountId, interest);
                    scheduleMapper.insertTransactionForFixedInterest(accountId, interest);
                    scheduleMapper.updateFixedDepositTime(fixedDepositId);
                } else { // 不续期
                    scheduleMapper.updateBalance(accountId, amount+interest);
                    scheduleMapper.insertTransactionForFixedInterest(accountId, amount+interest);
                    scheduleMapper.deleteFixedDeposit(fixedDepositId);
                }
            }
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            System.out.println("Error in fixedDepositInterestSettle");
        }
    }
}
