package com.mybank.module2_counter.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.mybank.module1_counter.entities.Customer;
import com.mybank.module1_counter.entities.SavingAccount;
import com.mybank.module1_counter.entities.TransactionInfo;
import com.mybank.module1_counter.mapper.CashierDutyMapper;
import com.mybank.module1_counter.request.TransferRequest;
import com.mybank.module2_counter.entities.Internet;
import com.mybank.module2_counter.mapper.CustomerMapper;
import com.mybank.module2_counter.mapper.InternetMapper;
import com.mybank.module2_counter.mapper.TransactionMapper;
import com.mybank.utils.ApiResult;
import com.mybank.utils.HashUtils;
import com.mybank.utils.JwtUtils;
import com.mybank.utils.UserUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@CrossOrigin
@RestController
public class InterentController {

    @Autowired
    InternetMapper internetMapper;
    @Autowired
    CustomerMapper customerMapper;
    @Autowired
    private CashierDutyMapper cashierDutyMapper;
    @Autowired
    private TransactionMapper transactionsMapper;


    @PostMapping("/internet/transfer")
    @Transactional
    public ApiResult transfer(@RequestBody TransferRequest txnRequest) {
        try{
            String accountId = txnRequest.getCardId();
            if(cashierDutyMapper.isDelete(accountId)) return ApiResult.failure("The card is already deleted");
            if(cashierDutyMapper.isFrozen(accountId)) return ApiResult.failure("The card is now frozen");
            if(cashierDutyMapper.isLost(accountId)) return ApiResult.failure("The card is now lost");

            String payeeId = txnRequest.getMoneyGoes();
            if(cashierDutyMapper.isDelete(payeeId)) return ApiResult.failure("The card is already deleted");
            if(cashierDutyMapper.isFrozen(payeeId)) return ApiResult.failure("The card is now frozen");
            if(cashierDutyMapper.isLost(payeeId)) return ApiResult.failure("The card is now lost");

            String hashPassword = HashUtils.md5Hash(txnRequest.getPassword());
            int ok = cashierDutyMapper.judgePassword(txnRequest.getCardId(), hashPassword);
            if(ok != 1) return ApiResult.failure("Password Error!");

            LocalDateTime now = LocalDateTime.now();
            TransactionInfo txn1 = new TransactionInfo();
            TransactionInfo txn2 = new TransactionInfo();
            txn1.setCardId(txnRequest.getCardId());
            txn2.setCardId(txnRequest.getMoneyGoes());
            txn1.setTransactionTime(now);
            txn2.setTransactionTime(now);
            txn1.setTransactionType("transaction");
            txn2.setTransactionType("transaction");
            txn1.setTransactionAmount(txnRequest.getTransactionAmount());
            txn2.setTransactionAmount(txnRequest.getTransactionAmount());
            txn1.setMoneySource(txnRequest.getCardId());
            txn2.setMoneySource(txnRequest.getCardId());
            txn1.setMoneyGoes(txnRequest.getMoneyGoes());
            txn2.setMoneyGoes(txnRequest.getMoneyGoes());
            txn1.setCurrency("CNY");
            txn1.setCardType("save");
            txn1.setTransactionChannel("internet");
            txn2.setCurrency("CNY");
            txn2.setCardType("save");
            txn2.setTransactionChannel("internet");
            cashierDutyMapper.insertTransaction(txn1);
            cashierDutyMapper.insertTransaction(txn2);
            cashierDutyMapper.updateAccountBalance(txnRequest.getCardId(), -txnRequest.getTransactionAmount());
            cashierDutyMapper.updateAccountBalance(txnRequest.getMoneyGoes(), txnRequest.getTransactionAmount());
            return ApiResult.success(txn1);

        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ApiResult.failure("Error transaction");
        }
//        System.out.println(loginRequest);
//        String fromAccountId = loginRequest.get("fromAccountId");
//        String toAccountId = loginRequest.get("toAccountId");
//        String password = loginRequest.get("password");
//        //String hashedPassword = HashUtils.md5Hash(password);
//        String money = loginRequest.get("money");
//        HashMap<String, String> user = UserUtils.get();
//        String customerId = user.get("customerId");
//        String customerAccountId = user.get("customerAccountId");
//        SavingAccount fromAccount = cashierDutyMapper.selectAccount(fromAccountId);
//        SavingAccount toAccount = cashierDutyMapper.selectAccount(toAccountId);
//        if (fromAccount == null) {
//            return ApiResult.failure(404, "付款储蓄卡不存在");
//        }
//        if (!StringUtils.equals(customerId, fromAccount.getCustomerId() + "")) {
//            return ApiResult.failure(404, "未查询到该储蓄卡");
//        }
//        if (fromAccount.getLossState()) {
//            return ApiResult.failure(404, "付款储蓄卡已挂失");
//        }
//        if (fromAccount.getFreezeState()) {
//            return ApiResult.failure(404, "付款储蓄卡已冻结");
//        }
//
//        if (toAccount == null) {
//            return ApiResult.failure(404, "收款储蓄卡不存在");
//        }
//        if (toAccount.getLossState()) {
//            return ApiResult.failure(404, "收款储蓄卡已挂失");
//        }
//        if (toAccount.getFreezeState()) {
//            return ApiResult.failure(404, "收款储蓄卡已冻结");
//        }
//
//
//
//        if (Double.parseDouble(money) > fromAccount.getBalance()) {
//            return ApiResult.failure(404, "余额不足");
//        }
//
//        if(cashierDutyMapper.judgePassword(fromAccountId,password)==0){
//            return ApiResult.failure("密码错误");
//        }
//
//        TransactionInfo fromTransactionInfo = new TransactionInfo();
//        fromTransactionInfo.setCardId(fromAccountId);
//        fromTransactionInfo.setCardType("储蓄卡");
//        fromTransactionInfo.setTransactionTime(LocalDateTime.now());
//        fromTransactionInfo.setTransactionAmount(Double.parseDouble(money));
//        fromTransactionInfo.setTransactionType("转账");
//        fromTransactionInfo.setTransactionChannel("网银");
//        fromTransactionInfo.setCurrency("CNY");
//        fromTransactionInfo.setMoneySource(fromAccountId);
//
//
//        TransactionInfo toTransactionInfo = new TransactionInfo();
//        toTransactionInfo.setCardId(fromAccountId);
//        toTransactionInfo.setCardType("储蓄卡");
//        toTransactionInfo.setTransactionTime(LocalDateTime.now());
//        toTransactionInfo.setTransactionAmount(Double.parseDouble(money));
//        toTransactionInfo.setTransactionType("收款");
//        toTransactionInfo.setTransactionChannel("网银");
//        toTransactionInfo.setCurrency("CNY");
//        toTransactionInfo.setMoneyGoes(fromAccountId);
//
//        transactionsMapper.insert(toTransactionInfo);
//        transactionsMapper.insert(fromTransactionInfo);
//        //此处应该用BigDecimal,为了时间
//        fromAccount.setBalance(fromAccount.getBalance() - Double.parseDouble(money));
//        toAccount.setBalance(toAccount.getBalance() + Double.parseDouble(money));
//
//        cashierDutyMapper.updateAccountBalance(fromAccount.getAccountId(),Double.valueOf("-" + money));
//        cashierDutyMapper.updateAccountBalance(toAccount.getAccountId(),Double.valueOf(money));
//
//
//        return ApiResult.success();
    }


    @PostMapping("/internet/history")
    public ApiResult history(@RequestBody Map<String, String> loginRequest) {
        String accountId = loginRequest.get("accountId");
//        HashMap<String, String> user = UserUtils.get();
        String customerId = loginRequest.get("customerId");
        SavingAccount account = cashierDutyMapper.selectAccount(accountId);
        if (account == null) {
            return ApiResult.failure(404, "未查询到该储蓄卡");
        }
//        if (!StringUtils.equals(customerId, account.getCustomerId() + "")) {
//            return ApiResult.failure(404, "未查询到该储蓄卡");
//        }
        if (account.getDeleted()) {
            return ApiResult.failure(404, "该卡已删除");
        }
        LambdaQueryWrapper<TransactionInfo> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(TransactionInfo::getCardId, accountId);
        List<TransactionInfo> transactionInfos = transactionsMapper.selectList(wrapper);
        return ApiResult.success(transactionInfos);
        //return ApiResult.success();
    }

    @GetMapping("/internet/query")
    public ApiResult query(@RequestParam String accountId) {
        System.out.println(accountId);
        //HashMap<String, String> user = UserUtils.get();
        //String customerId = user.get("customerId");
        SavingAccount account = cashierDutyMapper.selectAccount(accountId);
        System.out.println(account);
        if (account == null) {
            return ApiResult.failure(404, "未查询到该储蓄卡");
        }
//        if (StringUtils.equals(accountId, account.getCustomerId() + "")) {
//            return ApiResult.success(account);
//        }
        return ApiResult.success(account);
        //return ApiResult.failure(404, "未查询到该储蓄卡");
    }

    @PostMapping("/internet/register")
    public ApiResult register(@RequestBody Map<String, String> loginRequest) {
        System.out.println(loginRequest);
        //String customerName = loginRequest.get("customerName");
        String idNumber = loginRequest.get("idNumber");
        String phoneNumber = loginRequest.get("phoneNumber");
        String password = loginRequest.get("password");

        LambdaQueryWrapper<Customer> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Customer::getIdNumber, idNumber);
        wrapper.eq(Customer::getPhoneNumber, phoneNumber);
        //wrapper.eq(Customer::getCustomerName, customerName);


        Customer customer = customerMapper.selectOne(wrapper);
        if (customer == null) {
            return ApiResult.failure(900, "Customer info error");
        }
        Internet internet = new Internet();
        internet.setCustomerId(customer.getCustomerId());
        internet.setPassword(password);
        internet.setCreateTime(new Date());
        internet.setIsInBlackList(false);
        String customerAccountId = "92666" + String.format("%06d", customer.getCustomerId()) + String.format("%02d", customer.getCustomerId() + 1);
        internet.setCustomerAccountId(customerAccountId);
        internetMapper.insert(internet);
        return ApiResult.success(customerAccountId);
    }

    @PostMapping("/internet/login")
    public ApiResult cashierLogin(@RequestBody Map<String, String> loginRequest) {
        String customerAccountId = loginRequest.get("customerAccountId");
        String password = loginRequest.get("password");
        QueryWrapper<Internet> wrapper = new QueryWrapper<>();
        wrapper.eq("customer_account_id", customerAccountId);
        Internet internet = internetMapper.selectOne(wrapper);
        if (internet == null) {
            return ApiResult.failure(900, "CustomerAccountId not exists");
        }
        if (!StringUtils.equals(internet.getPassword(), password)) {
            return ApiResult.failure("password incorrect");
        }
        if (internet.getIsInBlackList()) {
            return ApiResult.failure("account in blacklist");
        }
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", "internet");
        claims.put("customerId", internet.getCustomerId());
        claims.put("customerAccountId", internet.getCustomerAccountId());
        String jwt = JwtUtils.generateJwt(claims);
        return ApiResult.success(jwt);
    }

    @PostMapping("/internet/query")
    public ApiResult queryAccounts(@RequestBody Map<String, String> loginRequest) {
        System.out.println(loginRequest);
        String customerAccountId = loginRequest.get("customerAccountId");
        String password = loginRequest.get("password");
        QueryWrapper<Internet> wrapper = new QueryWrapper<>();
        wrapper.eq("customer_account_id", customerAccountId);
        Internet internet = internetMapper.selectOne(wrapper);
        if (internet == null) {
            return ApiResult.failure(900, "CustomerAccountId not exists");
        }
        if (!StringUtils.equals(internet.getPassword(), password)) {
            return ApiResult.failure("password incorrect");
        }
        Integer customerId = internet.getCustomerId();
        List<String> customerAccountIds = cashierDutyMapper.selectAccountIdList(customerId);
        List<SavingAccount> savingAccounts = new ArrayList<>();
        for (String accountId : customerAccountIds) {
            savingAccounts.add(cashierDutyMapper.selectAccount(accountId));
        }
        return ApiResult.success(savingAccounts);
        //return ApiResult.success();
    }

    @PostMapping("/internet/reportLoss")
    public ApiResult reportLoss(@RequestBody Map<String, String> loginRequest) {
        System.out.println(loginRequest);
        String accountId = loginRequest.get("accountId");
        String password = loginRequest.get("password");
        try{
            SavingAccount account=cashierDutyMapper.selectAccount(accountId);
            if(account==null) return ApiResult.failure("not exists");
            if(account.getDeleted()) return ApiResult.failure("The card is already deleted");

            int ok = cashierDutyMapper.judgePassword(accountId, HashUtils.md5Hash(password));
            if(ok != 1) return ApiResult.failure("Password Error!");
            if(cashierDutyMapper.isLost(accountId)) return ApiResult.failure("already lost");

            cashierDutyMapper.changeLossState(accountId,true);
            cashierDutyMapper.insertLossRecord(accountId,LocalDateTime.now());
            return ApiResult.success(null);
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ApiResult.failure(e.getMessage());
        }
    }

}
