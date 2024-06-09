package com.mybank.module1_counter.mapper;

import com.mybank.module1_counter.entities.*;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface CashierDutyMapper {

    // 根据账号获取账户信息
    @Select("SELECT account_id,customer_id,customer_name,id_number,address,phone_number," +
            "balance,open_time,open_amount,freeze_state,loss_state,deleted " +
            "FROM saving_account NATURAL JOIN customer "+
            "WHERE account_id=#{accountId}")
    public SavingAccount selectAccount(String accountId);

    // 匹配账户密码
    @Select("select count(*) from saving_account where account_id=#{accountId} and password=#{password}")
    public int judgePassword(String accountId, String password);

    // 活期存款
    @Update("update saving_account set balance = balance+#{transactionAmount} where account_id = #{cardId}")
    public int updateAccountBalance(String cardId, Double transactionAmount);

    // 添加交易记录
    @Options(useGeneratedKeys = true, keyProperty = "transactionId")
    @Insert("insert into transaction(card_id, card_type, transaction_time, transaction_amount, transaction_type, transaction_channel, currency, money_source, money_goes) " +
            "values (#{cardId}, #{cardType}, #{transactionTime}, #{transactionAmount}, #{transactionType}, #{transactionChannel}, #{currency}, #{moneySource}, #{moneyGoes});")
    public int insertTransaction(TransactionInfo txn);

    // 定期存款
    @Options(useGeneratedKeys = true, keyProperty = "fixedDepositId")
    @Insert("insert into fixed_deposit(account_id, deposit_time, deposit_amount, deposit_type) " +
            "values ( #{accountId}, #{depositTime}, #{depositAmount}, #{depositType})")
    public int insertFixedDeposit(FixedDeposit fixedDeposit);

    // 获取定存记录
    @Select("select deposit_amount from fixed_deposit where fixed_deposit_id = #{fixedDepositId}")
    public Double getFixedDepositAmount(int fixedDepositId);

    // 删除定存记录（可以直接删？）
    @Delete("delete from fixed_deposit where fixed_deposit_id = #{fixedDepositId}")
    public int deleteFixedDeposit(int fixedDepositId);

    // 获取定期存款记录
    @Select("select * from fixed_deposit natural join fixed_deposit_type where account_id = #{accountId}")
    public List<FixedDeposit> showFixedDeposit(String accountId);



    @Select("SELECT customer_id FROM customer WHERE id_number=#{idNumber}")
    public Integer selectCustomer(String idNumber);


    @Options(useGeneratedKeys=true, keyProperty="customerId")
    @Insert("insert into customer(customer_name,id_number,phone_number,address) " +
            "values (#{customerName},#{idNumber},#{phoneNumber},#{address})")
    public int insertCustomer(SavingAccount account);

    @Insert("insert into saving_account(account_id,customer_id,password,balance,open_time,open_amount) " +
            "values (#{accountId},#{customerId},#{password},#{balance},#{openTime},#{openAmount})")
    public int insertAccount(SavingAccount account);

    // 冻结、挂失、删除 状态判断
    @Select("SELECT freeze_state FROM saving_account WHERE account_id=#{accountId}")
    public Boolean isFrozen(String accountId);
    @Select("SELECT loss_state FROM saving_account WHERE account_id=#{accountId}")
    public Boolean isLost(String accountId);
    @Select("SELECT deleted FROM saving_account WHERE account_id=#{accountId}")
    public Boolean isDelete(String accountId);

    @Select("SELECT deleted  FROM saving_account WHERE account_id=#{accountId}")
    public Boolean isDeleted(String accountId);
    @Update("UPDATE saving_account SET freeze_state=#{isFrozen} WHERE account_id=#{accountId}")
    public int changeFreezeState(String accountId, boolean isFrozen);
    @Update("UPDATE saving_account SET loss_state=#{isLost} WHERE account_id=#{accountId}")
    public int changeLossState(String accountId, boolean isLost);

    @Insert("insert into freeze_state_record(account_id,freeze_time,unfreeze_time,freeze_reason) " +
            "values (#{accountId},#{freezeTime},#{unfreezeTime},#{freezeReason});")
    public int insertFreezeRecord(String accountId, LocalDateTime freezeTime, LocalDateTime unfreezeTime, String freezeReason);

    @Insert("insert into loss_state_record(account_id,loss_time) " +
            "values (#{accountId},#{lossTime});")
    public int insertLossRecord(String accountId, LocalDateTime lossTime);

    @Update("UPDATE loss_state_record SET reissue_time=#{reissueTime} WHERE loss_state_record_id=#{lossStateRecordId}")
    public int changeLossRecord(int lossStateRecordId, LocalDateTime reissueTime);

    @Update("UPDATE freeze_state_record SET unfreeze_time=#{unfreezeTime} WHERE freeze_state_record_id=#{freezeStateRecordId}")
    public int changeFreezeRecord(int freezeStateRecordId, LocalDateTime unfreezeTime);

    @Select("SELECT freeze_state_record_id from freeze_state_record" +
            " WHERE account_id=#{accountId} ORDER BY freeze_time DESC LIMIT 1;")
    public Integer selectFreezeRecord(String accountId);
    @Select("SELECT loss_state_record_id from loss_state_record" +
            " WHERE account_id=#{accountId} ORDER BY loss_time DESC LIMIT 1;")
    public Integer selectLossRecord(String accountId);
    @Select("SELECT  COUNT(*) FROM saving_account WHERE customer_id=#{customerId}")
    public Integer selectAccountCount(int customerId);
    @Update("UPDATE saving_account SET deleted=#{isDeleted} WHERE account_id=#{accountId}")
    public int changeDeleteState(String accountId, boolean isDeleted);
    @Update("UPDATE saving_account SET password=#{password} WHERE account_id=#{accountId}")
    public int changePassword(String accountId, String password);


    @Select("SELECT freeze_time,unfreeze_time,freeze_reason from freeze_state_record" +
            " WHERE account_id=#{accountId} ORDER BY freeze_time DESC LIMIT 1;")
    public Map<String,String> selectFreezeRecordAllInfo(String accountId);


    @Select("select * from fixed_deposit_type order by deposit_duration")
    public List<FixedDepositType> selectFixedDepositType();

    // 获取客户信息
    @Select("select * from customer where id_number=#{idNumber}")
    public Customer selectOneCustomer(String idNumber);
    @Select("select account_id from saving_account where deleted=false and customer_id=#{customerId}")
    public List<String> selectAccountIdList(int customerId);

    // 修改客户信息
    @Update("update customer set customer_name=#{customerName},phone_number=#{phoneNumber}," +
            "address=#{address} where id_number=#{idNumber}")
    public int updateCustomer(Customer customer);

}
