package com.mybank.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilsTest {
    @Test
    public void test(){
        try {
            JwtUtils.parseJwt("");
            System.out.println("true");
        } catch (Exception e) {
            System.out.println("false");
        }
    }
}