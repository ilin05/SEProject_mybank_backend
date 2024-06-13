package com.mybank.module1_counter.controller;

import com.mybank.module1_counter.entities.Customer;
import com.mybank.module1_counter.request.DepositRequest;
import com.mybank.module1_counter.request.FreezeRequest;
import com.mybank.module1_counter.entities.SavingAccount;
import com.mybank.module1_counter.request.TransferRequest;
import com.mybank.module1_counter.request.WithdrawRequest;
import com.mybank.module1_counter.service.CashierDutyService;
import com.mybank.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE,RequestMethod.OPTIONS})
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
//        System.out.println(account);
        return cashierDutyService.openAccount(account);
    }
    @PostMapping("/closeAccount")
    public ApiResult closeAccount(@RequestBody Map<String,String> closeAccountInfo) {
        String accountId = closeAccountInfo.get("accountId");
        String password = closeAccountInfo.get("password");
        String idNumber = closeAccountInfo.get("idNumber");
        return cashierDutyService.closeAccount(accountId, password, idNumber);
    }



    // 存款：活期存款、定期存款
    @PostMapping("/demandDeposit")
    public ApiResult demandDeposit(@RequestBody DepositRequest demandDepositRequest){
        String accountId = demandDepositRequest.getAccountId();
        String password = demandDepositRequest.getPassword();
        Double amount = demandDepositRequest.getAmount();
        return cashierDutyService.demandDeposit(accountId, password, amount);
    }
    @PostMapping("/fixedDeposit")
    public ApiResult fixedDeposit(@RequestBody DepositRequest fixedDepositRequest){
        String accountId = fixedDepositRequest.getAccountId();
        String password = fixedDepositRequest.getPassword();
        String depositType = fixedDepositRequest.getDepositType();
        double amount = fixedDepositRequest.getAmount();
        return cashierDutyService.fixedDeposit(accountId, password, depositType, amount);
    }

    // 转账
    @PostMapping("/transfer")
    public ApiResult transfer(@RequestBody TransferRequest txnRequest){
        return cashierDutyService.transfer(txnRequest);
    }

    // 活期取款、定期取款
    @PostMapping("/withdrawDemand")
    public ApiResult withdrawDemandMoney(@RequestBody WithdrawRequest withdrawDemandRequest){
        String accountId = withdrawDemandRequest.getAccountId();
        String password = withdrawDemandRequest.getPassword();
        Double amount = withdrawDemandRequest.getAmount();
        return cashierDutyService.withdrawDemandMoney(accountId, password, amount);
    }
    @PostMapping("/withdrawFixed")
    public ApiResult withdrawFixedMoney(@RequestBody WithdrawRequest withdrawFixedRequest){
        int fixedDepositId = withdrawFixedRequest.getFixedDepositId();
        String accountId = withdrawFixedRequest.getAccountId();
        String password = withdrawFixedRequest.getPassword();
        double amount = withdrawFixedRequest.getAmount();
        return cashierDutyService.withdrawFixedMoney(fixedDepositId, accountId, password, amount);
    }

    // 状态改变：冻结解冻、挂失补发
    @PostMapping("/freeze")
    public ApiResult freezeAccount(@RequestBody FreezeRequest freezeRequest) {
        return cashierDutyService.freeze(freezeRequest);
    }
    @GetMapping("/unfreeze")
    public ApiResult unfreezeAccount(@RequestParam String accountId) {
        return cashierDutyService.getFrozenRecord(accountId);
    }
    @PostMapping("/unfreeze")
    public ApiResult unfreezeAccount(@RequestBody Map<String,String> unfreezeRequest) {
        String accountId = unfreezeRequest.get("accountId");
        String password = unfreezeRequest.get("password");
        return cashierDutyService.unfreeze(accountId, password);
    }
    @PostMapping("/reportLoss")
    public ApiResult reportLoss(@RequestBody Map<String,String> lossRequest) {
        String accountId = lossRequest.get("accountId");
        String password = lossRequest.get("password");
        return cashierDutyService.reportLoss(accountId, password);
    }
    @PostMapping("/reissue")
    public ApiResult reissueAccount(@RequestBody Map<String,String> reissueRequest) {
        String accountId = reissueRequest.get("accountId");
        String password = reissueRequest.get("password");
        return cashierDutyService.reissue(accountId, password);
    }

    // 修改储蓄卡支付密码
    @PostMapping("/modifyPassword")
    public ApiResult modifyPassword(@RequestBody Map<String,String> changePasswordRequest) {
        String accountId = changePasswordRequest.get("accountId");
        String oldPassword = changePasswordRequest.get("oldPassword");
        String newPassword = changePasswordRequest.get("newPassword");
        return cashierDutyService.modifyAccountPassword(accountId, oldPassword, newPassword);
    }


    @GetMapping("/fixedDepositType")
    public ApiResult getFixedDepositTypes() {
        return cashierDutyService.getFixedDepositTypes();
    }

    @GetMapping("/customerInfo")
    public ApiResult getCustomerInfo(@RequestParam String idNumber) {
        return cashierDutyService.getCustomerInfo(idNumber);
    }
    @PostMapping("/customerInfo")
    public ApiResult modifyCustomerInfo(@RequestBody Customer customer) {
        return cashierDutyService.updateCustomerInfo(customer);
    }
}