package sdu.codeeducationplat.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 禁用 CSRF
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // 无状态
                .authorizeHttpRequests(auth -> auth
                        // 公共接口（无需认证）
                        .requestMatchers("/api/public/users/register").permitAll()
                        .requestMatchers("/api/public/users/login").permitAll()
                        .requestMatchers("/api/public/admins/login").permitAll()
                        .requestMatchers("/api/public/schools").permitAll()
                        // Swagger 文档相关接口
                        .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/doc.html", "/webjars/**").permitAll()
                        // 管理员接口（需要 ADMIN 角色）
                        .requestMatchers("/api/admins/**").hasRole("ADMIN")
                        // 用户接口（需要认证，且角色为 USER、STUDENT 或 TEACHER）
                        .requestMatchers("/api/users/**").hasAnyRole("USER", "STUDENT", "TEACHER")
                        // 其他所有请求需要认证
                        .anyRequest().authenticated()
                )
                // 添加异常处理
                .exceptionHandling(exception -> exception
                        // 未认证用户（未登录）访问受保护接口，返回 401
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write("{\"code\": 401, \"message\": \"请先登录\", \"data\": null}");
                        })
                        // 已认证但权限不足用户，返回 403
                        .accessDeniedHandler((request, response, accessDeniedException) -> {
                            response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403 Forbidden
                            response.setContentType("application/json");
                            response.setCharacterEncoding("UTF-8");
                            response.getWriter().write("{\"code\": 403, \"message\": \"权限不足\", \"data\": null}");
                        })
                )
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}