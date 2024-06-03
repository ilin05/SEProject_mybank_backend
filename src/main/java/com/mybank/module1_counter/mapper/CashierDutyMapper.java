package com.mybank.module1_counter.mapper;

import com.mybank.module1_counter.entities.FixedDeposit;
import com.mybank.module1_counter.entities.SavingAccount;
import com.mybank.module1_counter.entities.TransactionInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CashierDutyMapper {

    //OK
    @Update("update saving_account set balance = balance+#{transactionAmount} where account_id = #{cardId}")
    public int updateAccountBalance(String cardId, Double transactionAmount);

    //OK
    @Insert("insert into transaction(transaction_id, card_id, card_type, transaction_time, transaction_amount, transaction_type, transaction_channel, currency, money_source, money_goes) values (#{transactionId}, #{cardId}, #{cardType}, #{transactionTime}, #{transactionAmount}, #{transactionType}, #{transactionChannel}, #{currency}, #{moneySource}, #{moneyGoes});")
    public int insertTransaction(TransactionInfo txn);
    //public List<Integer> selectAllCashier();

    //OK
    @Insert("insert into fixed_deposit(fixed_deposit_id, account_id, deposit_time, deposit_amount, deposit_type) values (#{fixedDepositId}, #{accountId}, #{depositTime}, #{depositAmount}, #{depositType})")
    public int insertFixedDeposit(FixedDeposit fixedDeposit);

    @Select("select deposit_amount from fixed_deposit where fixed_deposit_id = #{fixedDepositId}")
    public Double getFixedDepositAmount(int fixedDepositId);

    @Delete("delete from fixed_deposit where fixed_deposit_id = #{fixedDepositId}")
    public int deleteFixedDeposit(int fixedDepositId);


    @Select("select * from fixed_deposit where account_id = #{accountId}")
    public List<FixedDeposit> showFixedDeposit(String accountId);

    @Select("select account_id, customer_id, balance, freeze_state, loss_state, deleted, open_time, open_amount from saving_account where account_id = #{accountId}")
    public SavingAccount showAccountInfo(String accountId);

    @Select("select count(*) from saving_account where account_id = #{accountId} and password=#{password}")
    public int judgePassword(String accountId, String password);

}
