package sdu.codeeducationplat.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import sdu.codeeducationplat.common.Result;
import sdu.codeeducationplat.dto.LoginRequestDTO;
import sdu.codeeducationplat.dto.RegisterRequestDTO;
import sdu.codeeducationplat.dto.UserWithRoleDTO;
import sdu.codeeducationplat.model.Admin;
import sdu.codeeducationplat.model.School;
import sdu.codeeducationplat.model.User;
import sdu.codeeducationplat.model.enums.RoleEnum;
import sdu.codeeducationplat.service.AdminService;
import sdu.codeeducationplat.service.SchoolService;
import sdu.codeeducationplat.service.UserService;
import sdu.codeeducationplat.util.JwtUtil;

import java.util.List;

@Tag(name = "公共接口", description = "无需认证的公共接口，包括用户注册、登录和学校列表查询")
@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
public class UserPublicController {

    private final UserService userService;
    private final SchoolService schoolService;
    private final AdminService adminService;

    /**
     * 用户注册接口
     * @param dto 注册请求体，包含邮箱和密码
     * @return 注册结果，成功返回空，失败抛出异常
     */
    @Operation(summary = "用户注册", description = "通过邮箱和密码注册新用户，角色默认为 USER")
    @SecurityRequirement(name = "")
    @PostMapping("/users/register")
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
    @PostMapping("/users/login")
    public Result<String> login(@RequestBody LoginRequestDTO request) {
        UserWithRoleDTO dto = userService.login(request.getEmail(), request.getPassword());
        String token = JwtUtil.generateToken(dto.getUid(), dto.getRoles());
        return Result.success(token);
    }

    /**
     * 管理员登录接口
     * @param request 管理员登录请求体
     * @return 返回JWT Token
     */
    @Operation(summary = "管理员登录", description = "通过邮箱和密码登录，返回 JWT Token")
    @SecurityRequirement(name = "")
    @PostMapping("/admins/login")
    public Result<String> adminLogin(@RequestBody LoginRequestDTO request) {
        Admin admin = adminService.login(request.getEmail(), request.getPassword());
        String token = JwtUtil.generateToken(admin.getAdminId(), RoleEnum.ADMIN);
        return Result.success(token);
    }

    /**
     * 获取学校列表（用于下拉选择）
     * @param keyword 查询关键字，支持模糊查询学校名称以及代码
     * @return 返回学校列表
     */
    @Operation(summary = "获取学校列表（用于下拉选择）”改为“查询学校列表", description = "常用于下拉选择")
    @GetMapping("/schools")
    public Result<List<School>> listSchools(@RequestParam(required = false) String keyword) {
        return Result.success(schoolService.listSchoolsByKeyword(keyword));
    }

}
