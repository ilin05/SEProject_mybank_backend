package com.mybank.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.Map;

public class JwtUtils {
    private static final String SECRET_KEY;
    private static final Long EXPIRE_TIME = 3600*1000L;

    static {
        try {
            KeyGenerator kenGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey = kenGen.generateKey();
            SECRET_KEY = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateJwt(Map<String,Object> claims) {
        return Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .setClaims(claims)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE_TIME))
                .compact();
    }
    public static Claims parseJwt(String jwt) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(jwt)
                .getBody();
    }
    public static boolean validateJwt(String jwt) {
        try {
            JwtUtils.parseJwt(jwt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
