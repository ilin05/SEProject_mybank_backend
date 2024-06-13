package com.mybank.module4_creditcard.restapi;

/**
 * usage explanation:
 * success = true: query succeeded. the returning object will be stored in data
 * ----------------------------
 * success = false: query may be failed, and query may be OK but returning empty sets as result
 * code = error conditions
 * code < 0: returning empty sets as result. the message will say "undetermined" to show the backend does not know what happened
 * ( some functions does not have an exception handle mechanism, so I put a -1 code here )
 * code >= 0: a mistake appeared. the message will explain the exception condition
 */

public class GeneralResponse {
    private final boolean success;
    private final int code;
    private final String message;
    private final Object data;
    public GeneralResponse(final int code) {
        this.data = null;

        if(code >= 0) {
            this.success = true;
            this.code = code; // quick success
            this.message = "success";
        } else {
            this.success = false;
            this.code = code;
            this.message = "undetermined";
        }

    }
    public GeneralResponse(final Object data) {
        this.success = true;
        this.code = 0;
        this.message = "";
        this.data = data;
    }
    public GeneralResponse(final boolean success, final int code, final String message) {
        this.success = success;
        this.code = code;
        this.message = message;
        data = null;
    }
    public GeneralResponse(final boolean success, final int code, final String message, final Object data) {
        this.success = success;
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public boolean isSuccess() { return success; }
    public int getCode() { return code; }
    public String getMessage() { return message; }
    public Object getData() { return data; }
}
