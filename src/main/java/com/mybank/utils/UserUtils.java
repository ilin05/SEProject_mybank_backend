package com.mybank.utils;

import java.util.HashMap;

public class UserUtils {
    private static ThreadLocal<HashMap<String,String>> users = new ThreadLocal<>();

    public static void set(HashMap<String,String> params){
        users.set(params);
    }

    public static HashMap<String,String> get(){
        return users.get();
    }

    public static void remove(){
        users.remove();
    }

}
