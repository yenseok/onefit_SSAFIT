package com.ssafy.mvc.util;

import java.security.Key;
import java.util.Date;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {

    private static final String SECRET = "ssafysecretkeyssafysecretkeyssafysecretkey";  // 32byte 이상
    private static final Key key = Keys.hmacShaKeyFor(SECRET.getBytes());


    private static final long ACCESS_EXPIRATION = 1000L * 60 * 30*24;             // 30분
    private static final long REFRESH_EXPIRATION = 1000L * 60 * 60 * 24 * 7;   // 7일

    /**
     * Access Token 생성 (subject = userId, nickname은 claim)
     */
    public static String generateAccessToken(int userId, String nickname, String role) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("nickName", nickname)
                .claim("roles", role) //  파라미터로 받음
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION))
                .signWith(key)
                .compact();
    }



    /**
     * Refresh Token 생성 (subject = userId, nickname은 claim)
     */
    public static String generateRefreshToken(int userId, String nickname, int gymId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))          //  동일하게 userId로
                .claim("nickName", nickname)
                .claim("gymId", gymId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION))
                .signWith(key)
                .compact();
    }

    /**
     * 토큰에서 userId(subject) 추출
     */
    public static Integer getUserId(String token) {
        return Integer.parseInt(parseClaims(token).getSubject());
    }

    /**
     * 토큰에서 nickname 추출
     */
    public static String getNickName(String token) {
        Claims claims = parseClaims(token);
        Object value = claims.get("nickName");
        return value != null ? value.toString() : null;
    }

    /**
     * 토큰 유효성 검증
     */
    public static boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (ExpiredJwtException e) {
            System.out.println(" JWT 만료됨");
        } catch (JwtException e) {
            System.out.println(" JWT 유효하지 않음");
        }
        return false;
    }

    /**
     * Claims 추출 (내부용)
     */
    private static Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
    
    public static String getRole(String token) {
        Claims claims = parseClaims(token);
        Object role = claims.get("roles");
        return role != null ? role.toString() : null;
    }

}
