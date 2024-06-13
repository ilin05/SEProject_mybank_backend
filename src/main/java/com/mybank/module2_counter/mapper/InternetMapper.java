package com.mybank.module2_counter.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.mybank.module2_counter.entities.Internet;
import org.apache.ibatis.annotations.Select;

public interface InternetMapper extends BaseMapper<Internet> {
//    @Select("select * from internet where customer_account_id=#{customerAccountId} and password=#{password}")
//    public Internet judgePassword(String customerAccountId, String password);
//
//    @Select("select password from internet where customer_account_id = #{customerAccountId}")
//    public String getPassword(String customerAccountId);
}
