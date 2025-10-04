package com.itheima.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * JWT工具类，用于生成和解析JWT令牌
 * 优先从环境变量读取密钥，其次从配置文件读取
 */
@Slf4j
@Component
public class JwtUtils {

    @Value("${jwt.secret}")
    private String secretKeyFromConfig;
    
    @Value("${jwt.expire}")
    private int expireMinutes;
    
    // 获取JWT密钥，优先从环境变量读取
    private String getSecretKey() {
        // 从环境变量获取JWT密钥
        String secretFromEnv = System.getenv("JWT_SECRET");
        
        // 如果环境变量中存在密钥，则使用环境变量中的密钥
        if (secretFromEnv != null && !secretFromEnv.trim().isEmpty()) {
            log.debug("使用环境变量中的JWT密钥");
            return secretFromEnv;
        }
        
        // 否则使用配置文件中的密钥
        log.debug("使用配置文件中的JWT密钥");
        return secretKeyFromConfig;
    }

    /**
     * 生成JWT令牌
     */
    public String generateJwt(Map<String, Object> claims) {
        // 计算过期时间（当前时间 + 配置的分钟数）
        long expireTime = System.currentTimeMillis() + (long) expireMinutes * 60 * 1000;
        
        String jwt = Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, getSecretKey())
                .setExpiration(new Date(expireTime))
                .compact();
        return jwt;
    }

    /**
     * 解析JWT令牌
     */
    public Claims parseJwt(String jwt) {
        Claims claims = Jwts.parser()
                .setSigningKey(getSecretKey())
                .parseClaimsJws(jwt)
                .getBody();
        return claims;
    }

}
