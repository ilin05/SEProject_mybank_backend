package com.mybank.module4_creditcard.mapper;
import org.apache.ibatis.annotations.*;

@Mapper
public interface AuditorMapper {
    @Select("select count(*) from auditors where auditor_id = #{auditorId} and password = #{password}")
    public int checkPassword(int auditorId, String password);
}
