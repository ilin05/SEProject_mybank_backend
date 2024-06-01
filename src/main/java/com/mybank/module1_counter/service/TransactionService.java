package com.mybank.module1_counter.service;

import com.mybank.module1_counter.entities.FixedDeposit;
import com.mybank.utils.ApiResult;
import org.apache.ibatis.transaction.Transaction;

public interface TransactionService {
    // current deposit
    ApiResult currentDeposit(Transaction transaction);

    // withdraw money
    ApiResult withdrawMoney(Transaction transaction);

    // fixed deposit
    ApiResult fixedDeposit(FixedDeposit fixedDeposit);

    // transfer
    ApiResult transferAccounts(Transaction transaction);
}
