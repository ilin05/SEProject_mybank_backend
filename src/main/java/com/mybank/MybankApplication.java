package com.mybank;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@MapperScan("com.mybank.*.mapper")
public class MybankApplication {

    public static void main(String[] args) {
        SpringApplication.run(MybankApplication.class, args);
    }

}
