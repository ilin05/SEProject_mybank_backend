package com.mybank.module1_counter.service;

import com.mybank.module1_counter.entities.Cashier;
import com.mybank.module1_counter.mapper.CashierManageMapper;
import com.mybank.utils.ApiResult;
import com.mybank.utils.HashUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CashierManageServiceImpl implements CashierManageService {

    @Autowired
    private CashierManageMapper cashierManageMapper;

    @Override
    public ApiResult getCashier() {
        List<Cashier> CashierList = cashierManageMapper.selectAllCashier();
        return ApiResult.success(CashierList);
    }

    @Override
    public ApiResult addCashier(Cashier cashier) {
        try {
            System.out.println(cashier);
            String idNumber = cashier.getIdNumber();
            String lastSix = idNumber.substring(idNumber.length() - 6);
            cashier.setPassword(HashUtils.md5Hash(HashUtils.sha256Hash(lastSix)));
            System.out.println("hello2");
            cashierManageMapper.insertCashier(cashier);
            System.out.println("hello1");
            cashier.setPassword(null);
            return ApiResult.success(cashier);
        } catch (Exception e) {
            return ApiResult.failure("Error inserting cashier");
        }
    }

    @Override
    public ApiResult modifyCashier(Cashier cashier) {
        try {
            cashierManageMapper.updateCashier(cashier);
            return ApiResult.success(null);
        } catch (Exception e) {
            return ApiResult.failure("Error modifying cashier");
        }
    }

    @Override
    public ApiResult removeCashier(Integer cashierId) {
        try {
            cashierManageMapper.deleteCashier(cashierId);
            return ApiResult.success(null);
        } catch (Exception e) {
            return ApiResult.failure("Error removing cashier");
        }
    }
}