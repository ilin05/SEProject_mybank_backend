package com.mybank.module1_counter.mapper;

import com.mybank.module1_counter.entities.Cashier;
import com.mybank.module1_counter.entities.SavingAccount;
import com.mybank.module1_counter.entities.TransactionInfo;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.transaction.Transaction;

import java.util.List;

@Mapper
public interface CashierDutyMapper {

    @Update("update saving_account set balance = balance+#{transactionAmount} where account_id = #{cardId}")
    @Insert("insert into transaction values (#{transactionId}, #{account_id}, #{cardType}, #{transactionTime}, #{transactionAmount}, #{transactionType}, #{transactionChannel}, #{currency}, #{moneySource}, #{moneyGoes});")
    public int demandDeposit(TransactionInfo txn);
    //public List<Integer> selectAllCashier();

    @Insert("insert into transaction values (#{transactionId}, #{account_id}, #{cardType}, #{transactionTime}, #{transactionAmount}, #{transactionType}, #{transactionChannel}, #{currency}, #{moneySource}, #{moneyGoes});")
    public int fixedDeposit(TransactionInfo txn);

    @Select("select * from fixed_deposit where account_id = #{cardId}")
    public List<TransactionInfo> showFixedDeposit(String cardId);

    @Select("select * from saving_account where account_id = #{cardId}")
    public List<SavingAccount> showDemandDeposit(String cardId);

    @Update("update cashier set cashier_name=#{name} where cashier_id=#{id}")
    public int updateCashier(Cashier cashier);


    @Delete("delete from cashier where cashier_id=#{id}")
    public int deleteCashier(Integer id);
}
