package com.mybank.module1_counter.controller;

import com.mybank.module1_counter.queries.FreezeInfo;
import com.mybank.module1_counter.entities.SavingAccount;
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

    @PostMapping("/demandDeposit")
    public ApiResult demandDeposit(@RequestBody Map<String, Object> demandDepositRequest){
        String accountId = (String) demandDepositRequest.get("accountId");
        String password = (String) demandDepositRequest.get("password");
        Double amount = (Double) demandDepositRequest.get("amount");
        return cashierDutyService.demandDeposit(accountId, password, amount);
    }

    @GetMapping("/fixedDeposit")
    public ApiResult getDemandDeposit(@RequestParam String accountId){
        return cashierDutyService.showFixedDeposit(accountId);
    }

    @PostMapping("/fixedDeposit")
    public ApiResult fixedDeposit(@RequestBody Map<String, Object> fixedDepositRequest){
        String accountId = (String) fixedDepositRequest.get("accountId");
        String password = (String) fixedDepositRequest.get("password");
        String depositType = (String) fixedDepositRequest.get("depositType");
        Double amount = (Double) fixedDepositRequest.get("amount");
        return cashierDutyService.fixedDeposit(accountId, password, depositType, amount);
    }


    @PostMapping("/transfer")
    public ApiResult transfer(@RequestBody TransferRequest txnRequest){
//        System.out.println(txnRequest);
        return cashierDutyService.transfer(txnRequest);
    }

    @PostMapping("/withdrawDemand")
    public ApiResult withdrawDemandMoney(@RequestBody Map<String, Object> withdrawDemandRequest){
        String accountId = (String) withdrawDemandRequest.get("accountId");
        String password = (String) withdrawDemandRequest.get("password");
        Double amount = (Double) withdrawDemandRequest.get("amount");
        return cashierDutyService.withdrawDemandMoney(accountId, password, amount);
    }

    @PostMapping("/withdrawFixed")
    public ApiResult withdrawFixedMoney(@RequestBody Map<String, Object> withdrawFixedRequest){
        int fixedDepositId = (int) withdrawFixedRequest.get("fixedDepositId");
        String accountId = (String) withdrawFixedRequest.get("accountId");
        String password = (String) withdrawFixedRequest.get("password");
        Double amount = (Double) withdrawFixedRequest.get("amount");
        return cashierDutyService.withdrawFixedMoney(fixedDepositId, accountId, password, amount);
    }



    @GetMapping("/accountInfo")
    public ApiResult getAccount(@RequestParam String accountId) {
        return cashierDutyService.getAccount(accountId);
    }
    @PostMapping("/openAccount")
    public ApiResult addAccount(@RequestBody SavingAccount account) {
        return cashierDutyService.openAccount(account);
    }
    @PostMapping("/closeAccount")
    public ApiResult closeAccount(@RequestBody Map<String,String> closeAccoountInfo) {
        return cashierDutyService.closeAccount(closeAccoountInfo.get("accountId"),closeAccoountInfo.get("password"),closeAccoountInfo.get("idNumber"));
    }
    @PostMapping("/freeze")
    public ApiResult freezeAccount(@RequestBody FreezeInfo freezeInfo) {
        System.out.println(freezeInfo.getUnfreezeTime());
        return cashierDutyService.freeze(freezeInfo);
    }
    @PostMapping("/unfreeze")
    public ApiResult unfreezeAccount(@RequestBody Map<String,String> unfreezeRequest) {
        return cashierDutyService.unfreeze(unfreezeRequest.get("accountId"));
    }
    @PostMapping("/reportLoss")
    public ApiResult reportLoss(@RequestBody Map<String,String> lossRequest) {
        return cashierDutyService.reportLoss(lossRequest.get("accountId"));
    }
    @PostMapping("/reissue")
    public ApiResult reissueAccount(@RequestBody Map<String,String> reissueRequest) {
        return cashierDutyService.reissue(reissueRequest.get("accountId"));
    }
    @PostMapping("/modifyPassword")
    public ApiResult modifyPassword(@RequestBody Map<String,String> changePasswordRequest) {
        String accountId = (String) changePasswordRequest.get("accountId");
        String oldPassword = (String) changePasswordRequest.get("oldPassword");
        String newPassword = (String) changePasswordRequest.get("newPassword");
        return cashierDutyService.modifyAccountPassword(accountId, oldPassword, newPassword);
    }

}