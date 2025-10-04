package com.itheima.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * JWT工具类，用于生成和解析JWT令牌
 * 作为Spring组件，从配置文件中读取JWT配置
 */
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secretKey;
    
    @Value("${jwt.expire}")
    private int expireMinutes;

    /**
     * 生成JWT令牌
     */
    public String generateJwt(Map<String, Object> claims) {
        // 计算过期时间（当前时间 + 配置的分钟数）
        long expireTime = System.currentTimeMillis() + (long) expireMinutes * 60 * 1000;
        
        String jwt = Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .setExpiration(new Date(expireTime))
                .compact();
        return jwt;
    }

    /**
     * 解析JWT令牌
     */
    public Claims parseJwt(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }

}
