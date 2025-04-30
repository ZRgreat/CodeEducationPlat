package sdu.codeeducationplat.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import sdu.codeeducationplat.model.enums.RoleEnum;
import sdu.codeeducationplat.util.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, jakarta.servlet.ServletException {
        // 跳过 OPTIONS 预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);
            filterChain.doFilter(request, response);
            return;
        }

        // 跳过公开端点
        String path = request.getRequestURI();
        if (path.startsWith("/api/public/") || path.startsWith("/swagger-ui/") || path.startsWith("/v3/api-docs/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            try {
                if (JwtUtil.isTokenValid(token)) {
                    Claims claims = JwtUtil.parseToken(token);
                    Object principal;
                    List<RoleEnum> roles;

                    // 判断 token 类型
                    if (claims.containsKey("uid")) {
                        // 用户 token
                        Long uid = JwtUtil.getUidFromToken(token);
                        roles = JwtUtil.getRolesFromToken(token);
                        principal = uid;
                    } else if (claims.containsKey("adminId")) {
                        // 管理员 token
                        Long adminId = JwtUtil.getAdminIdFromToken(token);
                        RoleEnum role = JwtUtil.getRoleFromToken(token);
                        roles = Collections.singletonList(role);
                        principal = adminId;
                    } else {
                        throw new RuntimeException("Token 中缺少 uid 或 adminId");
                    }

                    // 确保角色列表不为空
                    if (roles == null || roles.isEmpty()) {
                        throw new RuntimeException("用户未分配角色");
                    }

                    // 将 List<RoleEnum> 转换为 List<SimpleGrantedAuthority>
                    List<SimpleGrantedAuthority> authorities = roles.stream()
                            .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                            .collect(Collectors.toList());

                    // 创建认证对象
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                            principal, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (JwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.setContentType("application/json");
                response.getWriter().write("{\"code\": 401, \"msg\": \"Invalid or expired token: " + e.getMessage() + "\", \"data\": null}");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
}