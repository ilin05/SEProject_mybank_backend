package com.mybank.module1_counter.service;

import com.mybank.module1_counter.entities.Cashier;
import com.mybank.module1_counter.mapper.CashierMapper;
import com.mybank.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CashierServiceImpl implements CashierService {

    @Autowired
    private CashierMapper cashierMapper;

    @Override
    public ApiResult getCashier() {
        List<Integer> integerList = cashierMapper.selectAllCashier();
        return ApiResult.success(integerList);
    }

    @Override
    public ApiResult addCashier(Cashier cashier) {
        int result = cashierMapper.insertCashier(cashier);
        return ApiResult.success(cashier);
    }

    @Override
    public ApiResult modifyCashier(Cashier cashier) {
        int result = cashierMapper.updateCashier(cashier);
        return ApiResult.success(cashier);
    }

    @Override
    public ApiResult removeCashier(Integer cashierId) {
        int result = cashierMapper.deleteCashier(cashierId);
        return ApiResult.success(cashierId);
    }
}