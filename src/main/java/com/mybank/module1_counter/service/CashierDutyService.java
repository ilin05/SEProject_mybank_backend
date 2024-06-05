package com.mybank.module1_counter.service;

import com.mybank.module1_counter.queries.FreezeInfo;
import com.mybank.module1_counter.entities.SavingAccount;
import com.mybank.module1_counter.queries.TransferRequest;
import com.mybank.utils.ApiResult;

public interface CashierDutyService {

    ApiResult demandDeposit(String accountId, String password, Double amount);

    ApiResult fixedDeposit(String accountId, String password, String depositType, Double amount);

    ApiResult showFixedDeposit(String accountId);

    ApiResult withdrawDemandMoney(String accountId, String password, Double amount);

    ApiResult withdrawFixedMoney(int fixedDepositId, String accountId, String password, Double amount);

    ApiResult transfer(TransferRequest txnRequest);




    ApiResult getAccount(String accountId);
    ApiResult openAccount(SavingAccount account);
    ApiResult freeze(FreezeInfo freezeInfo);
    ApiResult unfreeze(String accountId);
    ApiResult reportLoss(String accountId);
    ApiResult reissue(String accountId);

}
