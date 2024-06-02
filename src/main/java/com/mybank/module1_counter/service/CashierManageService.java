package com.mybank.module1_counter.service;

import com.mybank.module1_counter.entities.Cashier;
import com.mybank.utils.ApiResult;

public interface CashierManageService {

    // select
    ApiResult getCashier();

    // insert
    ApiResult addCashier(Cashier cashier);

    // update
    ApiResult modifyCashier(Cashier cashier);

    // remove
    ApiResult removeCashier(Integer cashierId);

}
