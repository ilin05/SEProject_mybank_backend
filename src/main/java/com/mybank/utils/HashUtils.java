package com.mybank.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class HashUtils {
    public static String md5Hash(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(input.getBytes());
            String hashString = new String(hashBytes, StandardCharsets.ISO_8859_1);
            return hashString;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
