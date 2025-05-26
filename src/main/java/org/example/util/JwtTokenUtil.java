package org.example.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.example.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Component
public class JwtTokenUtil {
    private static final String BLACKLIST_PREFIX = "jwt:blacklist:";

    private final SecretKey secretKey;
    private final long expiration;
    private final RedisTemplate<String, String> redisTemplate;

    @Autowired
    public JwtTokenUtil(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.expiration}") long expiration,
            RedisTemplate<String, String> redisTemplate) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes());
        this.expiration = expiration;
        this.redisTemplate = redisTemplate;
    }

    public String generateToken(User user) {
        String tokenId = UUID.randomUUID().toString();

        return Jwts.builder()
                .setId(tokenId) // 设置唯一ID用于黑名单管理
                .setSubject(user.getUsername())
                .claim("userId", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration * 1000))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }

    /**
     * 使Token失效
     */
    public void invalidateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            String jti = claims.getId(); // 获取Token的唯一标识
            Date expiration = claims.getExpiration();

            // 计算剩余有效时间(毫秒)
            long ttl = expiration.getTime() - System.currentTimeMillis();

            if (ttl > 0) {
                // 将Token ID加入黑名单，并设置与Token相同的过期时间
                redisTemplate.opsForValue().set(
                        BLACKLIST_PREFIX + jti,
                        "invalid",
                        ttl,
                        TimeUnit.MILLISECONDS
                );
            }
        } catch (Exception e) {
            // Token解析失败说明已经是无效Token
        }
    }

    /**
     * 验证Token有效性
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = getClaimsFromToken(token);
            String jti = claims.getId();

            // 检查Token是否在黑名单中
            if (Boolean.TRUE.equals(redisTemplate.hasKey(BLACKLIST_PREFIX + jti))) {
                return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Claims getClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}