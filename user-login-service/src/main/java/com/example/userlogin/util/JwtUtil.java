package com.example.userlogin.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * JWT 工具类 - 生成和解析 JWT token
 */
@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long expirationMs;
    private static final String CLAIM_USER_ID = "userId";
    private static final String CLAIM_USERNAME = "username";

    public JwtUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration-ms}") long expirationMs) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMs = expirationMs;
    }

    /**
     * 生成 JWT token
     */
    public String generateToken(Long userId, String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .claim(CLAIM_USER_ID, userId)
                .claim(CLAIM_USERNAME, username)
                .subject(username)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(secretKey)
                .compact();
    }

    /**
     * 解析并校验 JWT token，返回 Claims
     *
     * @return Claims 或 null（token 无效时）
     */
    public Claims parseToken(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        try {
            return Jwts.parser()
                    .verifyWith(secretKey)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
        } catch (JwtException e) {
            return null;
        }
    }

    /**
     * 从 Claims 获取 userId
     */
    public Long getUserId(Claims claims) {
        if (claims == null) return null;
        Object v = claims.get(CLAIM_USER_ID);
        if (v instanceof Number) {
            return ((Number) v).longValue();
        }
        return null;
    }

    /**
     * 获取 token 过期时间（秒），用于 Max-Age
     */
    public int getExpirationSeconds() {
        return (int) (expirationMs / 1000);
    }
}
