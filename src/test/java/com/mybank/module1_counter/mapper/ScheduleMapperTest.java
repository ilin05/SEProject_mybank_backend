package com.mybank.module1_counter.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ScheduleMapperTest {

    @Autowired
    private ScheduleMapper mapper;

    @Test
    public void test1() {
        mapper.updateInterest(0.5);
    }
    @Test
    public void test2() {
        mapper.updateAllBalance();
    }

}