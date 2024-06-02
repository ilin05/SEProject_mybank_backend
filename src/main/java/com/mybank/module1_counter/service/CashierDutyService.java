package com.mybank.module1_counter.service;

import com.mybank.module1_counter.entities.*;
import com.mybank.utils.ApiResult;

public interface CashierDutyService {

    ApiResult demandDeposit(TransactionInfo txn);

    ApiResult fixedDeposit(TransactionInfo txn);

    ApiResult showDeposit(String cardId);

    ApiResult withdrawMoney(TransactionInfo txn);

}
