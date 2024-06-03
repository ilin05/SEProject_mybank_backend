package com.mybank.module1_counter.service;

import com.mybank.module1_counter.entities.FixedDeposit;
import com.mybank.module1_counter.entities.SavingAccount;
import com.mybank.module1_counter.entities.TransactionInfo;
import com.mybank.module1_counter.mapper.CashierDutyMapper;
import com.mybank.module1_counter.queries.*;
import com.mybank.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CashierDutyServiceImpl implements CashierDutyService {
    @Autowired
    private CashierDutyMapper cashierDutyMapper;

    @Override
    public ApiResult demandDeposit(String accountId, String password, Double amount) {
        try{
            int ok = cashierDutyMapper.judgePassword(accountId, password);
            if(ok == 1){
                TransactionInfo txn = new TransactionInfo();
                txn.setTransactionId(1);
                txn.setCardId(accountId);
                txn.setMoneyGoes(accountId);
                //java.util.Date date = new java.util.Date();
                //Timestamp timestamp = new Timestamp(date.getTime());
                //txn.setTransactionTime(timestamp);
                txn.setTransactionTime(LocalDateTime.now());
                //System.out.println("Time: "+LocalDateTime.now());
                txn.setTransactionType("demand_deposit");
                txn.setTransactionAmount(amount);
                txn.setCurrency("CNY");
                txn.setCardType("save");
                txn.setTransactionChannel("cashier");

                cashierDutyMapper.updateAccountBalance(accountId, amount);
                cashierDutyMapper.insertTransaction(txn);
                return ApiResult.success(null);
            }else{
                return ApiResult.failure("Password Error!");
            }
        } catch (Exception e) {
            return ApiResult.failure("Error demand deposit");
        }
    }

    @Override
    public ApiResult fixedDeposit(String accountId, String password, String depositType, Double amount) {
        try{
            int ok = cashierDutyMapper.judgePassword(accountId, password);
            if(ok == 1){
                FixedDeposit fixedDeposit = new FixedDeposit();
                fixedDeposit.setFixedDepositId(2);
                fixedDeposit.setDepositTime(LocalDateTime.now());
                fixedDeposit.setDepositAmount(amount);
                fixedDeposit.setAccountId(accountId);
                fixedDeposit.setDepositType(depositType);
                System.out.println(fixedDeposit);
                cashierDutyMapper.insertFixedDeposit(fixedDeposit);
                return ApiResult.success(fixedDeposit.getFixedDepositId());
            }else{
                return ApiResult.failure("Password Error!");
            }
        } catch (Exception e) {
            return ApiResult.failure("Error fixed deposit");
        }
    }

    @Override
    public ApiResult showFixedDeposit(String accountId) {
        try{
            List<FixedDeposit> fixedDeposits = cashierDutyMapper.showFixedDeposit(accountId);

            return ApiResult.success(fixedDeposits);
        } catch (Exception e){
            return ApiResult.failure("Error showDemandDeposit");
        }
    }

    @Override
    public ApiResult showAccountInfo(String accountId) {
        try{
            SavingAccount account = cashierDutyMapper.showAccountInfo(accountId);
            return ApiResult.success(account);
        } catch (Exception e) {
            return ApiResult.failure("Error showAccountInfo");
        }

        //return null;
    }

    @Override
    public ApiResult transfer(TransferRequest txnRequest) {
        try{
            int ok = cashierDutyMapper.judgePassword(txnRequest.getCardId(), txnRequest.getPassword());
            if(ok == 1){
                LocalDateTime now = LocalDateTime.now();
                TransactionInfo txn1 = new TransactionInfo();
                TransactionInfo txn2 = new TransactionInfo();
                txn1.setTransactionId(3);
                txn2.setTransactionId(4);
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
                txn1.setTransactionChannel("cashier");
                txn2.setCurrency("CNY");
                txn2.setCardType("save");
                txn2.setTransactionChannel("cashier");
                cashierDutyMapper.insertTransaction(txn1);
                cashierDutyMapper.insertTransaction(txn2);
                cashierDutyMapper.updateAccountBalance(txnRequest.getCardId(), -txnRequest.getTransactionAmount());
                cashierDutyMapper.updateAccountBalance(txnRequest.getMoneyGoes(), txnRequest.getTransactionAmount());
                return ApiResult.success("OK!");
            }else{
                return ApiResult.failure("Password Error!");
            }
        } catch (Exception e) {
            return ApiResult.failure("Error transaction");
        }
    }


    @Override
    public ApiResult withdrawDemandMoney(String accountId, String password, Double amount) {
        try{
            int ok = cashierDutyMapper.judgePassword(accountId, password);
            if(ok == 1){
                TransactionInfo txn = new TransactionInfo();
                txn.setTransactionId(6);
                txn.setCardId(accountId);
                txn.setTransactionTime(LocalDateTime.now());
                txn.setTransactionType("withdrawDemand");
                txn.setTransactionAmount(amount);
                txn.setMoneySource(accountId);
                txn.setCurrency("CNY");
                txn.setCardType("save");
                txn.setTransactionChannel("cashier");
                cashierDutyMapper.updateAccountBalance(accountId, -amount);
                cashierDutyMapper.insertTransaction(txn);
                return ApiResult.success(null);
            }else{
                return ApiResult.failure("Password Error!");
            }
        } catch (Exception e) {
            return ApiResult.failure("Error withdrawDemandMoney");
        }
    }

    @Override
    public ApiResult withdrawFixedMoney(int fixedDepositId, String accountId, String password, Double amount) {
        try{
            int ok = cashierDutyMapper.judgePassword(accountId, password);
            if(ok == 1){
                TransactionInfo txn = new TransactionInfo();
                txn.setTransactionId(5);
                txn.setCardId(accountId);
                txn.setTransactionTime(LocalDateTime.now());
                txn.setTransactionType("withdrawFixedDeposit");
                txn.setMoneySource(accountId);
                txn.setCurrency("CNY");
                txn.setCardType("save");
                txn.setTransactionChannel("cashier");
                Double transferAmount = cashierDutyMapper.getFixedDepositAmount(fixedDepositId) - amount;
                txn.setTransactionAmount(transferAmount);
                cashierDutyMapper.insertTransaction(txn);
                cashierDutyMapper.updateAccountBalance(accountId, transferAmount);
                cashierDutyMapper.deleteFixedDeposit(fixedDepositId);
                return ApiResult.success(null);
            }else{
                return ApiResult.failure("Password Error!");
            }
        } catch (Exception e) {
            return ApiResult.failure("Error withdraw fixed deposit");
        }
    }

}
