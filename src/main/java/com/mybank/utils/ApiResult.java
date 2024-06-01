package com.mybank.utils;

public class ApiResult {
    public Boolean code;
    public String message;
    public Object payload;

    public ApiResult(boolean code, String message) {
        this.code = code;
        this.message = message;
    }
    public ApiResult(boolean code, Object payload) {
        this.code = code;
        this.payload = payload;
    }
    public ApiResult(boolean code, String message, Object payload) {
        this.code = code;
        this.message = message;
        this.payload = payload;
    }

    public static ApiResult success(Object payload) {
        return new ApiResult(true, "success", payload);
    }
    public static ApiResult failure(String message) {
        return new ApiResult(false, message, null);
    }

    @Override
    public String toString() {
        return "ApiResult{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", payload=" + payload +
                '}';
    }
}