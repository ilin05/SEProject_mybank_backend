package com.mybank.module1_counter.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDateTime;

@Mapper
public interface TestMapper {
    @Insert("insert into table_name(timetest) values (#{atime} )")
    int insertTest(LocalDateTime atime);
}
