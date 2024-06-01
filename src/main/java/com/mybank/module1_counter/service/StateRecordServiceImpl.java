package com.mybank.module1_counter.service;

import com.mybank.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StateRecordServiceImpl implements StateRecordService {


    @Override
    public ApiResult freeze() {
        return null;
    }

    @Override
    public ApiResult unfreeze() {
        return null;
    }

    @Override
    public ApiResult lossReport() {
        return null;
    }

    @Override
    public ApiResult reissueReport() {
        return null;
    }
}
