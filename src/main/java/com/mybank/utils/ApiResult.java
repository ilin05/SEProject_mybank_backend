package com.mybank.utils;

import lombok.ToString;

@ToString
public class ApiResult {
    public Integer code;
    public String message;
    public Object payload;

    public ApiResult(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
    public ApiResult(Integer code, Object payload) {
        this.code = code;
        this.payload = payload;
    }
    public ApiResult(Integer code, String message, Object payload) {
        this.code = code;
        this.message = message;
        this.payload = payload;
    }


    public static ApiResult success(Object payload) {
        return new ApiResult(1, "success", payload);
    }
    public static ApiResult failure(String message) {
        return new ApiResult(0, message, null);
    }
}