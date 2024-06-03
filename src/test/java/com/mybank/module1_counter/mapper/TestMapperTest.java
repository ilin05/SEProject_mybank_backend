package com.mybank.module1_counter.mapper;

import org.apache.catalina.mapper.Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TestMapperTest {
    @Autowired
    private TestMapper testMapper;

    @Test
    void testMapper() {
        testMapper.insertTest(LocalDateTime.now());
    }
}