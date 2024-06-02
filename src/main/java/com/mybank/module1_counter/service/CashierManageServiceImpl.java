package com.mybank.module1_counter.service;

import com.mybank.module1_counter.entities.Cashier;
import com.mybank.module1_counter.mapper.CashierManageMapper;
import com.mybank.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CashierManageServiceImpl implements CashierManageService {

    @Autowired
    private CashierManageMapper cashierManageMapper;

    @Override
    public ApiResult getCashier() {
        List<Integer> integerList = cashierManageMapper.selectAllCashier();
        return ApiResult.success(integerList);
    }

    @Override
    public ApiResult addCashier(Cashier cashier) {
        int result = cashierManageMapper.insertCashier(cashier);
        return ApiResult.success(cashier);
    }

    @Override
    public ApiResult modifyCashier(Cashier cashier) {
        int result = cashierManageMapper.updateCashier(cashier);
        return ApiResult.success(cashier);
    }

    @Override
    public ApiResult removeCashier(Integer cashierId) {
        int result = cashierManageMapper.deleteCashier(cashierId);
        return ApiResult.success(cashierId);
    }
}