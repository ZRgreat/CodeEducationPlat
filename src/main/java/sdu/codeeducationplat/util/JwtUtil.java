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

    // 为用户生成JWT
    public static String generateToken(Long uid, List<RoleEnum> roles) {
        List<String> roleValues = roles.stream().map(RoleEnum::getValue).collect(Collectors.toList());
        Map<String, Object> claims = new HashMap<>();
        claims.put("uid", uid);
        claims.put("roles", roleValues); // 使用 value
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    // 为管理员生成JWT
    public static String generateToken(Long adminId, RoleEnum role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("adminId", adminId);
        claims.put("role", role.getValue());
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET_KEY)
                .compact();
    }

    // 解析JWT
    public static Claims parseToken(String token) throws JwtException {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(SECRET_KEY)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();
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

    // 获取UID
    public static Long getUidFromToken(String token) {
        return parseToken(token).get("uid", Long.class);
    }

    // 获取AdminId
    public static Long getAdminIdFromToken(String token) {
        return parseToken(token).get("adminId", Long.class);
    }

    // 获取用户角色列表
    public static List<RoleEnum> getRolesFromToken(String token) {
        @SuppressWarnings("unchecked")
        List<String> roleValues = parseToken(token).get("roles", List.class);
        return roleValues.stream().map(RoleEnum::fromValue).collect(Collectors.toList());
    }

    // 获取管理员角色
    public static RoleEnum getRoleFromToken(String token) {
        String role = parseToken(token).get("role", String.class);
        return RoleEnum.fromValue(role);
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