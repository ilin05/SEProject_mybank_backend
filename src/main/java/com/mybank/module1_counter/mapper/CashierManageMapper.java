package com.mybank.module1_counter.mapper;

import com.mybank.module1_counter.entities.Cashier;
import com.mybank.module2_counter.entities.Internet;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface CashierManageMapper {

    @Select("select cashier_id, cashier_name, id_number, phone_number, address, privilege from cashier " +
            "where cashier_id=#{cashierId} and password=#{password}")
    public Cashier getOneCashier(long cashierId, String password);

    @Select("select cashier_id, cashier_name, id_number, phone_number, address, privilege from cashier")
    public List<Cashier> selectAllCashier();


    @Options(useGeneratedKeys=true, keyProperty="cashierId")
    @Insert("insert into cashier(cashier_name, id_number, phone_number, password, address, privilege) values (#{cashierName},#{idNumber},#{phoneNumber},#{password},#{address},#{privilege})")
    public int insertCashier(Cashier cashier);


    @Update("update cashier set cashier_name=#{cashierName}, id_number=#{idNumber}, phone_number=#{phoneNumber}, " +
            "address=#{address}, privilege=#{privilege} where cashier_id=#{cashierId}")
    public int updateCashier(Cashier cashier);


    @Delete("delete from cashier where cashier_id=#{cashierId}")
    public int deleteCashier(Integer cashierId);

    @Update("update internet set is_in_black_list = true where customer_account_id=#{id}")
    public int updateInBlackList(String id);

    @Update("update internet set is_in_black_list = false where customer_account_id=#{internetId}")
    public int updateOutBlackList(String internetId);

    @Select("select id,customer_account_id,customer_id,is_in_black_list,password from internet")
    public List<Internet> selectInternet();
}
