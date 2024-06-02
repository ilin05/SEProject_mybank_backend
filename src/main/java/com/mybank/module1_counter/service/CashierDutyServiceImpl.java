package com.mybank.module1_counter.service;

import com.mybank.module1_counter.entities.SavingAccount;
import com.mybank.module1_counter.entities.TransactionInfo;
import com.mybank.module1_counter.mapper.CashierDutyMapper;
import com.mybank.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CashierDutyServiceImpl implements CashierDutyService {
    @Autowired
    private CashierDutyMapper cashierDutyMapper;

    @Override
    public ApiResult demandDeposit(TransactionInfo txn) {
        int res = cashierDutyMapper.demandDeposit(txn);
        return ApiResult.success(res);
    }

    @Override
    public ApiResult fixedDeposit(TransactionInfo txn) {
        int res = cashierDutyMapper.fixedDeposit(txn);
        return ApiResult.success(res);
    }

    @Override
    public ApiResult showDeposit(String cardId) {
        List<SavingAccount> savingAccounts = cashierDutyMapper.showDemandDeposit(cardId);
        List<TransactionInfo> fixedDeposits = cashierDutyMapper.showFixedDeposit(cardId);

        return ApiResult.success(fixedDeposits);
        //return null;
    }

    @Override
    public ApiResult withdrawMoney(TransactionInfo txn) {
        return null;
    }

}
