package com.mybank.module1_counter.controller;

import com.mybank.module1_counter.queries.FixedDepositRequest;
import com.mybank.module1_counter.queries.TransferRequest;
import com.mybank.module1_counter.service.CashierDutyService;
import com.mybank.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/cashier")
public class CashierDutyController {
    @Autowired
    private CashierDutyService cashierDutyService;

    @GetMapping("/accountinfo")
    public ApiResult getAccountInfo(@RequestParam String accountId){
        return cashierDutyService.showAccountInfo(accountId);
    }

    @PostMapping("/demanddeposit")
    public ApiResult demandDeposit(@RequestBody Map<String, Object> demandDepositRequest){
        String accountId = (String) demandDepositRequest.get("accountId");
        String password = (String) demandDepositRequest.get("password");
        Double amount = (Double) demandDepositRequest.get("amount");
        //System.out.printf(accountId,password,amount);
        return cashierDutyService.demandDeposit(accountId, password, amount);
    }

    @GetMapping("/fixeddeposit")
    public ApiResult getDemandDeposit(@RequestParam String accountId){
        return cashierDutyService.showFixedDeposit(accountId);
    }

    @PostMapping("/fixeddeposit")
    public ApiResult fixedDeposit(@RequestBody Map<String, Object> fixedDepositRequest){
        String accountId = (String) fixedDepositRequest.get("accountId");
        String password = (String) fixedDepositRequest.get("password");
        String depositType = (String) fixedDepositRequest.get("depositType");
        Double amount = (Double) fixedDepositRequest.get("amount");
        return cashierDutyService.fixedDeposit(accountId, password, depositType, amount);
    }


//    @PostMapping("/fixeddeposit")
//    public ApiResult fixedDeposit(@RequestBody FixedDepositRequest fixedDepositRequest){
//        return cashierDutyService.fixedDeposit(fixedDepositRequest);
//    }

    @PostMapping("/transfer")
    public ApiResult transfer(@RequestBody TransferRequest txnRequest){
        return cashierDutyService.transfer(txnRequest);
    }

    @PostMapping("/withdrawdemand")
    public ApiResult withdrawDemandMoney(@RequestBody Map<String, Object> withdrawDemandRequest){
        String accountId = (String) withdrawDemandRequest.get("accountId");
        String password = (String) withdrawDemandRequest.get("password");
        Double amount = (Double) withdrawDemandRequest.get("amount");
        return cashierDutyService.withdrawDemandMoney(accountId, password, amount);
    }

    @PostMapping("/withdrawfixed")
    public ApiResult withdrawFixedMoney(@RequestBody Map<String, Object> withdrawFixedRequest){
        int fixedDepositId = (int) withdrawFixedRequest.get("fixedDepositId");
        String accountId = (String) withdrawFixedRequest.get("accountId");
        String password = (String) withdrawFixedRequest.get("password");
        Double amount = (Double) withdrawFixedRequest.get("amount");
        return cashierDutyService.withdrawFixedMoney(fixedDepositId, accountId, password, amount);
    }



}
