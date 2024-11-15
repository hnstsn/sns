package com.example.sns.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtTokenUtils {

    public static String getUserName(String token, String key) {
        return extractClaims(token, key).get("userName", String.class);
    }

    public static boolean isExpired(String token, String key) {
        Date expirationDate = extractClaims(token, key).getExpiration();
        return expirationDate.before(new Date());
    }

    private static Claims extractClaims(String token, String key) {
        return Jwts.parser().verifyWith(getSecretKey(key))
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public static String generateToken(String userName, String key, long expiredTimeMs) {
        Claims claims = Jwts.claims()
                .add("userName", userName)
                .build();

        return Jwts.builder()
                .claims(claims)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expiredTimeMs))
                .signWith(getSecretKey(key))
                .compact();
    }


    private static SecretKey getSecretKey(String key) {
//        byte[] keyBytes = Decoders.BASE64.decode(key);
        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
