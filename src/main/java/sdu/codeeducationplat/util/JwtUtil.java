package sdu.codeeducationplat.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JwtUtil {
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 7 * 24 * 3600_000; // 7 天

    // 生成 JWT
    public static String generateToken(String uid, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", uid);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    // 解析 JWT
    public static Claims parseToken(String token) throws JwtException {
        try {
            return Jwts.parser() // 直接使用 parser()
                    .verifyWith(SECRET_KEY) // 设置签名密钥
                    .build() // 构建 JwtParser
                    .parseSignedClaims(token) // 解析并验证签名
                    .getPayload(); // 获取 Claims
        } catch (ExpiredJwtException e) {
            throw new RuntimeException("Token 已过期", e);
        } catch (SignatureException e) {
            throw new RuntimeException("Token 签名无效", e);
        } catch (MalformedJwtException e) {
            throw new RuntimeException("Token 格式错误", e);
        } catch (JwtException e) {
            throw new RuntimeException("Token 解析失败", e);
        }
    }

    // 获取 UID
    public static String getUidFromToken(String token) {
        return parseToken(token).get("uid", String.class);
    }

    // 获取角色
    public static String getRoleFromToken(String token) {
        return parseToken(token).get("role", String.class);
    }

    // 验证 Token 是否有效
    public static boolean isTokenValid(String token) {
        try {
            parseToken(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}