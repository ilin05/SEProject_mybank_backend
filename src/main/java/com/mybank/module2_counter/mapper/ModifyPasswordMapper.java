package com.mybank.module2_counter.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface ModifyPasswordMapper {
    @Select("select count(*) from internet where customer_account_id = #{id} and password=#{password}")
    public int SelectInternets(String id, String password);

    @Update("update internet set password=#{password} where customer_account_id=#{id}")
    public int UpdatePassword(String id, String password);
}
