package com.mybank.module1_counter.service;

import com.mybank.module1_counter.queries.FixedDepositRequest;
import com.mybank.module1_counter.queries.TransferRequest;
import com.mybank.utils.ApiResult;

public interface CashierDutyService {

    ApiResult demandDeposit(String accountId, String password, Double amount);

    //ApiResult fixedDeposit(FixedDepositRequest fixedDepositRequest);
    ApiResult fixedDeposit(String accountId, String password, String depositType, Double amount);

    ApiResult showFixedDeposit(String accountId);

    ApiResult showAccountInfo(String accountId);

    ApiResult withdrawDemandMoney(String accountId, String password, Double amount);

    ApiResult withdrawFixedMoney(int fixedDepositId, String accountId, String password, Double amount);

    ApiResult transfer(TransferRequest txnRequest);

}
