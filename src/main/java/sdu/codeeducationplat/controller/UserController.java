package sdu.codeeducationplat.controller;

import io.jsonwebtoken.JwtException;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import sdu.codeeducationplat.common.Result; // 确保正确导入 Result
import sdu.codeeducationplat.dto.*;
import sdu.codeeducationplat.model.User;
import sdu.codeeducationplat.service.UserService;
import sdu.codeeducationplat.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 用户控制器类，处理用户相关的 API 请求
 */
@Tag(name = "用户管理", description = "用户相关的 API 接口，包括注册、登录、修改昵称等")
@RestController
@RequestMapping("/api/auth")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册接口
     * @param dto 注册请求体，包含邮箱和密码
     * @return 注册结果，成功返回空，失败抛出异常
     */
    @Operation(summary = "用户注册", description = "通过邮箱和密码注册新用户，角色默认为 STUDENT")
    @SecurityRequirement(name = "")
    @PostMapping("/register")
    public Result<String> register(@RequestBody RegisterRequestDTO dto) {
        User user = userService.register(dto);
        String yourUid = "注册成功，UID: " + String.format("%06d", user.getUid());
        return Result.success(yourUid);
    }

    /**
     * 用户登录接口
     * @param request 登录请求体，包含邮箱和密码
     * @return 登录结果，成功返回 JWT Token，失败抛出异常
     */
    @Operation(summary = "用户登录", description = "通过邮箱和密码登录，返回 JWT Token")
    @SecurityRequirement(name = "")
    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginRequestDTO request) {
        UserWithRoleDTO dto = userService.login(request.getEmail(), request.getPassword());
        String token = JwtUtil.generateToken(dto.getUid(), dto.getRoles());
        return Result.success(token);
    }

    /**
     * 修改用户昵称接口
     * @param request 请求体，包含新昵称
     * @return 修改结果，成功返回空，失败抛出异常
     */
    @Operation(summary = "修改昵称", description = "修改当前登录用户的昵称，需要登录认证")
    @PostMapping("/update-nickname")
    @PreAuthorize("isAuthenticated() and !hasRole('ADMIN') and hasAnyRole('USER', 'STUDENT', 'TEACHER')")
    public Result<Void> updateNickname(@RequestBody Map<String, String> request) {
        String token = getTokenFromRequest();
        Long uid = JwtUtil.getUidFromToken(token);
        userService.updateNickname(uid, request.get("nickname"));
        return Result.success();
    }

    /**
     * 修改用户头像接口
     * @param request 请求体，包含头像 URL
     * @return 修改结果，成功返回新头像 URL，失败抛出异常
     */
    @Operation(summary = "修改头像", description = "修改当前登录用户的头像（URL 形式），需要登录认证")
    @PostMapping("/update-avatar")
    @PreAuthorize("isAuthenticated() and !hasRole('ADMIN') and hasAnyRole('USER', 'STUDENT', 'TEACHER')")
    public Result<String> updateAvatar(@RequestBody Map<String, String> request) {
        String token = getTokenFromRequest();
        Long uid = JwtUtil.getUidFromToken(token);
        String avatar = userService.updateAvatar(uid, request.get("avatar"));
        return Result.success(avatar);
    }

    /**
     * 获取用户详情接口
     * @return 用户详情，成功返回用户信息，失败抛出异常
     */
    @Operation(summary = "获取当前登录用户信息", description = "获取当前登录用户的基本信息")
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public Result<UserProfileDTO> getUserDetails() {
        String token = getTokenFromRequest();
        Long uid = JwtUtil.getUidFromToken(token);
        UserProfileDTO dto = userService.getUserDetails(uid);
        return Result.success(dto);
    }

    /**
     * 用户注销（物理删除）
     * @return
     */
    @Operation(summary = "注销账号", description = "注销当前登录用户")
    @DeleteMapping("/delete")
    @PreAuthorize("isAuthenticated() and !hasRole('ADMIN') and hasAnyRole('USER', 'STUDENT', 'TEACHER')")
    public Result<Void> deleteAccount() {
        String token = getTokenFromRequest(); // 从 Header 获取 Bearer Token
        Long uid = JwtUtil.getUidFromToken(token); // 从 Token 中解析 UID
        userService.deactivateUser(uid); // removeById(uid)，物理删除
        return Result.success();
    }

    /**
     * 从请求头中提取 JWT Token
     * @return JWT Token 字符串
     * @throws RuntimeException 如果 Authorization 头无效
     */
    private String getTokenFromRequest() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new JwtException("无效的 Authorization 头");
        }
        return authHeader.substring(7);
    }
}