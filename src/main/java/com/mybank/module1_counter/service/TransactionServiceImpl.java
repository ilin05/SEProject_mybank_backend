package com.mybank.module1_counter.service;

import com.mybank.module1_counter.entities.FixedDeposit;
import com.mybank.module1_counter.mapper.TransactionMapper;
import com.mybank.utils.ApiResult;
import org.apache.ibatis.transaction.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private TransactionMapper transactionMapper;

    @Override
    public ApiResult currentDeposit(Transaction transaction) {
        return ApiResult.success(transaction);
    }

    @Override
    public ApiResult withdrawMoney(Transaction transaction) {
        return null;
    }

    @Override
    public ApiResult fixedDeposit(FixedDeposit fixedDeposit) {
        return null;
    }

    @Override
    public ApiResult transferAccounts(Transaction transaction) {
        return null;
    }
}
