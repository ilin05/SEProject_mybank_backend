package com.mybank.module3_loan.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface LoanMapper {

    @Select("select count(*) from reviewer where id=#{loanId} and password=#{password}")
    public int judgePassword(int loanId, String password);
}
