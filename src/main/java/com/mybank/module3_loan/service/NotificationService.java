package com.mybank.module3_loan.service;

import com.mybank.module3_loan.model.Customer;
import com.mybank.module3_loan.model.LoanApplication;
import com.mybank.module3_loan.repository.CustomerRepository;
import com.mybank.module3_loan.repository.LoanApplicationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class NotificationService {


    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private LoanApplicationRepository loanApplicationRepository;

    // 缓存用户的贷款通知信息
    private ConcurrentHashMap<Long, Boolean> loanNotificationCache = new ConcurrentHashMap<>();
    // 缓存用户紧急需要还款的贷款ID列表
    private ConcurrentHashMap<Long, List<Long>> urgentLoanIdsCache = new ConcurrentHashMap<>();

    // 定时任务每天检查一次
    @Scheduled(cron = "0 0 0 * * ?")
    public void checkLoanStatus() {
        List<Customer> customers = customerRepository.findAll();
        for (Customer customer : customers) {
            boolean notification = false;
            List<Long> urgentLoanIds = loanApplicationRepository.findByCustomerId(customer.getCustomerId()).stream()
                    .filter(loan -> {
                        LocalDate now = LocalDate.now();
                        LocalDate loanEndDate = loan.getLoanEndDate();
                        return loanEndDate != null &&
                                (loanEndDate.isEqual(now) || loanEndDate.isBefore(now) || loanEndDate.isEqual(now.plusDays(60)));
                    })
                    .map(LoanApplication::getLoanId)
                    .collect(Collectors.toList());

            if (!urgentLoanIds.isEmpty()) {
                notification = true;
            }

            loanNotificationCache.put(customer.getCustomerId(), notification);
            urgentLoanIdsCache.put(customer.getCustomerId(), urgentLoanIds);
        }
    }

    // 获取用户的贷款通知状态
    public boolean getLoanNotification(Long customerId) {
        return loanNotificationCache.getOrDefault(customerId, false);
    }

    // 获取用户紧急需要还款的贷款ID列表
    public List<Long> getUrgentLoanIds(Long customerId) {
        return urgentLoanIdsCache.getOrDefault(customerId, List.of());
    }
}
