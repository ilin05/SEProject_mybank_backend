package com.mybank.module1_counter.mapper;

import com.mybank.module1_counter.entities.FixedDeposit;
import org.apache.ibatis.annotations.*;

import java.util.List;


@Mapper
public interface ScheduleMapper {

    // 每天算利息
    @Update("update saving_account set interest=interest+balance*#{rate}")
    public void updateInterest(double rate);

    // 三个月结息一次
    @Update("update saving_account set balance=balance+interest,interest=0")
    public void updateAllBalance();

    // 添加结息交易记录
    @Insert("insert into transaction(card_id, card_type, transaction_time, transaction_amount, transaction_type, transaction_channel, currency, money_source, money_goes) " +
            "select account_id, 'save', NOW(), interest, '结息', null, 'CNY', null, account_id " +
            "from saving_account")
    public void insertTransactionForDemandInterest();


    // 获取冻结的账户列表
    @Select("select account_id from saving_account where freeze_state=true")
    public List<String> selectFrozenAccountList();

    // 找到最近一次冻结记录
    @Select("select freeze_state_record_id from freeze_state_record " +
            "where account_id=#{accountId} ORDER BY freeze_time DESC LIMIT 1")
    public Integer selectFreezeRecord(String accountId);

    // 更新解冻日期 (返回值1，代表真的解冻；0代表未到期)
    @Update("update freeze_state_record set unfreeze_time = NOW() " +
            "where freeze_state_record_id=#{freezeRecordId} and NOW()>unfreeze_time")
    public int updateUnfreezeTime(int freezeRecordId);

    // 为某个账户解冻
    @Update("update saving_account set freeze_state=false where account_id=#{accountId}")
    public void updateFreezeRecord(String accountId);


    // 所有定期记录
    @Select("select * from fixed_deposit natural join fixed_deposit_type")
    public List<FixedDeposit> selectFixedDepositList();

    // 更新余额
    @Update("update saving_account set balance=balance+#{amount} where account_id=#{accountId}")
    public void updateBalance(String accountId, double amount);

    // 添加交易记录
    @Insert("insert into transaction(card_id, card_type, transaction_time, transaction_amount, transaction_type, transaction_channel, currency, money_source, money_goes) " +
            "values(#{accountId}, 'save', NOW(), #{amount}, '定期结息', null, 'CNY', null, #{accountId})")
    public void insertTransactionForFixedInterest(String accountId, double amount);

    // 删除定存记录
    @Delete("delete from fixed_deposit where fixed_deposit_id=#{fixedDepositId}")
    public void deleteFixedDeposit(int fixedDepositId);

    // 定期存款修改：改开始时间
    @Update("update fixed_deposit set deposit_time=NOW() where fixed_deposit_id=#{fixedDepositId}")
    public void updateFixedDepositTime(int fixedDepositId);
}
