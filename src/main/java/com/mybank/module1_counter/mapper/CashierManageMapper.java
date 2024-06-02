package com.mybank.module1_counter.mapper;

import com.mybank.module1_counter.entities.Cashier;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CashierManageMapper {

    @Select("select cashier_id from cashier")
    public List<Integer> selectAllCashier();


    @Options(useGeneratedKeys=true, keyProperty="cashierId")
    @Insert("insert into cashier(cashier_id) values (#{cashierId})")
    public int insertCashier(Cashier cashier);


    @Update("update cashier set cashier_name=#{name} where cashier_id=#{id}")
    public int updateCashier(Cashier cashier);


    @Delete("delete from cashier where cashier_id=#{id}")
    public int deleteCashier(Integer id);
}