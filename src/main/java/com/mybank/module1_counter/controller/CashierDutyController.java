package com.mybank.module1_counter.controller;

import com.mybank.module1_counter.request.FreezeInfo;
import com.mybank.module1_counter.entities.SavingAccount;
import com.mybank.module1_counter.request.TransferRequest;
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

    // 获取账户信息、定期存款记录
    @GetMapping("/accountInfo")
    public ApiResult getAccount(@RequestParam String accountId) {
        return cashierDutyService.getAccountInfo(accountId);
    }
    @GetMapping("/fixedDeposit")
    public ApiResult getDemandDeposit(@RequestParam String accountId){
        return cashierDutyService.showFixedDeposit(accountId);
    }

    // 开户、销户
    @PostMapping("/openAccount")
    public ApiResult addAccount(@RequestBody SavingAccount account) {
        return cashierDutyService.openAccount(account);
    }
    @PostMapping("/closeAccount")
    public ApiResult closeAccount(@RequestBody Map<String,String> closeAccountInfo) {
        String accountId = (String) closeAccountInfo.get("accountId");
        String password = (String) closeAccountInfo.get("password");
        String idNumber = (String) closeAccountInfo.get("idNumber");
        return cashierDutyService.closeAccount(accountId, password, idNumber);
    }

    // 存款：活期存款、定期存款
    @PostMapping("/demandDeposit")
    public ApiResult demandDeposit(@RequestBody Map<String, Object> demandDepositRequest){
        String accountId = (String) demandDepositRequest.get("accountId");
        String password = (String) demandDepositRequest.get("password");
        Double amount = (Double) demandDepositRequest.get("amount");
        return cashierDutyService.demandDeposit(accountId, password, amount);
    }
    @PostMapping("/fixedDeposit")
    public ApiResult fixedDeposit(@RequestBody Map<String, Object> fixedDepositRequest){
        String accountId = (String) fixedDepositRequest.get("accountId");
        String password = (String) fixedDepositRequest.get("password");
        String depositType = (String) fixedDepositRequest.get("depositType");
        Double amount = (Double) fixedDepositRequest.get("amount");
        return cashierDutyService.fixedDeposit(accountId, password, depositType, amount);
    }

    // 转账
    @PostMapping("/transfer")
    public ApiResult transfer(@RequestBody TransferRequest txnRequest){
        return cashierDutyService.transfer(txnRequest);
    }

    // 活期存款、定期存款
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

    // 状态改变：冻结解冻、挂失补发
    @PostMapping("/freeze")
    public ApiResult freezeAccount(@RequestBody FreezeInfo freezeInfo) {
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

    // 修改储蓄卡支付密码
    @PostMapping("/modifyPassword")
    public ApiResult modifyPassword(@RequestBody Map<String,String> changePasswordRequest) {
        String accountId = (String) changePasswordRequest.get("accountId");
        String oldPassword = (String) changePasswordRequest.get("oldPassword");
        String newPassword = (String) changePasswordRequest.get("newPassword");
        return cashierDutyService.modifyAccountPassword(accountId, oldPassword, newPassword);
    }

}