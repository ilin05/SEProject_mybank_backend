package com.mybank.module1_counter.service;

import com.mybank.module1_counter.request.FreezeInfo;
import com.mybank.module1_counter.entities.SavingAccount;
import com.mybank.module1_counter.request.TransferRequest;
import com.mybank.utils.ApiResult;

public interface CashierDutyService {

    // 获取账户信息、定存记录
    ApiResult getAccountInfo(String accountId);

    ApiResult showFixedDeposit(String accountId);

    // 开户、销户
    ApiResult openAccount(SavingAccount account);

    ApiResult closeAccount(String accountId, String password, String idNumber);

    // 活期存款、定期存款
    ApiResult demandDeposit(String accountId, String password, Double amount);

    ApiResult fixedDeposit(String accountId, String password, String depositType, Double amount);

    // 活期取款、定期取款
    ApiResult withdrawDemandMoney(String accountId, String password, Double amount);

    ApiResult withdrawFixedMoney(int fixedDepositId, String accountId, String password, Double amount);

    // 转账
    ApiResult transfer(TransferRequest txnRequest);

    // 状态改变
    ApiResult freeze(FreezeInfo freezeInfo);

    ApiResult unfreeze(String accountId);

    ApiResult reportLoss(String accountId);

    ApiResult reissue(String accountId);

    // 修改支付密码
    ApiResult modifyAccountPassword(String accountId, String oldPassword, String newPassword);
}
