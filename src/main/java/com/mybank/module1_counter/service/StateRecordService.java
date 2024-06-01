package com.mybank.module1_counter.service;

import com.mybank.utils.ApiResult;

public interface StateRecordService {
    ApiResult freeze();
    ApiResult unfreeze();
    ApiResult lossReport();
    ApiResult reissueReport();
}
