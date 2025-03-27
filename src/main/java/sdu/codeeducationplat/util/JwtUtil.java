package sdu.codeeducationplat.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import sdu.codeeducationplat.model.enums.RoleEnum;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

public class JwtUtil {
    private static final SecretKey SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    private static final long EXPIRATION_TIME = 7 * 24 * 3600_000; // 7 天

    // 生成 JWT
    public static String generateToken(Long uid, List<RoleEnum> roles) {
        List<String> roleNames = roles.stream().map(RoleEnum::name).collect(Collectors.toList());
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", uid);
        claims.put("roles", roleNames);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    public static String generateToken(Long adminId, RoleEnum role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("adminId", adminId);
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
             Claims claims =  Jwts.parser() // 直接使用 parser()
                    .verifyWith(SECRET_KEY) // 设置签名密钥
                    .build() // 构建 JwtParser
                    .parseSignedClaims(token) // 解析并验证签名
                    .getPayload(); // 获取 Claims
            return claims;
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
    public static Long getUidFromToken(String token) {
        return parseToken(token).get("uid", Long.class);
    }

    //获取AdminId
    public static Long getAdminIdFromToken(String token) {
        return parseToken(token).get("adminId", Long.class);
    }

    // 获取用户角色
    public static List<RoleEnum> getRolesFromToken(String token) {
        @SuppressWarnings("unchecked")
        List<String> roleNames = parseToken(token).get("roles", List.class);
        return roleNames.stream().map(RoleEnum::valueOf).collect(Collectors.toList());
    }
    // 获取管理员角色
    public static RoleEnum getRoleFromToken(String token) {
        @SuppressWarnings("unchecked")
        String role = parseToken(token).get("role", String.class); // 获取字符串形式的 role
        return RoleEnum.valueOf(role); // 转换为 RoleEnum
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