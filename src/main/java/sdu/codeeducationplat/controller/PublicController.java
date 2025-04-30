package sdu.codeeducationplat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import sdu.codeeducationplat.common.Result;
import sdu.codeeducationplat.dto.LoginRequestDTO;
import sdu.codeeducationplat.dto.RegisterRequestDTO;
import sdu.codeeducationplat.dto.UserWithRoleDTO;
import sdu.codeeducationplat.model.enums.RoleEnum;
import sdu.codeeducationplat.model.user.Admin;
import sdu.codeeducationplat.model.school.School;
import sdu.codeeducationplat.model.user.User;
import sdu.codeeducationplat.model.enums.ResultCode;
import sdu.codeeducationplat.service.AdminService;
import sdu.codeeducationplat.service.SchoolService;
import sdu.codeeducationplat.service.UserService;
import sdu.codeeducationplat.util.JwtUtil;

import java.util.List;

@Tag(name = "公共接口", description = "无需认证的公共接口，包括用户注册、登录和学校列表查询")
@RestController
@RequestMapping("/api/public")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class PublicController {

    private final UserService userService;
    private final SchoolService schoolService;
    private final AdminService adminService;

    @Operation(summary = "用户注册", description = "通过邮箱和密码注册新用户，角色默认为 USER")
    @SecurityRequirement(name = "")
    @PostMapping("/register")
    public Result<?> register(@RequestBody RegisterRequestDTO dto) {
        log.info("用户注册，dto：{}", dto);
        try {
            UserWithRoleDTO result = userService.register(dto);
            log.info("用户注册成功，uid：{}", result.getUid());
            return Result.success(result);
        } catch (Exception e) {
            log.error("用户注册失败，dto：{}，错误：", dto, e);
            return Result.error(ResultCode.SERVER_ERROR, "注册失败: " + e.getMessage());
        }
    }

    @Operation(summary = "用户登录", description = "通过邮箱和密码登录，返回 JWT Token 和角色信息")
    @SecurityRequirement(name = "")
    @PostMapping("/login")
    public Result<?> login(@RequestBody LoginRequestDTO request) {
        log.info("用户登录，email：{}", request.getEmail());
        try {
            UserWithRoleDTO dto = userService.login(request.getEmail(), request.getPassword());
            log.info("用户登录成功，uid：{}", dto.getUid());
            return Result.success(dto);
        } catch (Exception e) {
            log.error("用户登录失败，email：{}，错误：", request.getEmail(), e);
            return Result.error(ResultCode.UNAUTHORIZED, "登录失败: " + e.getMessage());
        }
    }

    @Operation(summary = "管理员登录", description = "通过邮箱和密码登录，返回 JWT Token")
    @SecurityRequirement(name = "")
    @PostMapping("/admin/login")
    public Result<?> adminLogin(@RequestBody LoginRequestDTO request) {
        log.info("管理员登录，email：{}", request.getEmail());
        try {
            Admin admin = adminService.login(request.getEmail(), request.getPassword());
            String token = JwtUtil.generateToken(admin.getAdminId(), RoleEnum.ADMIN);
            log.info("管理员登录成功，adminId：{}", admin.getAdminId());
            return Result.success(token);
        } catch (Exception e) {
            log.error("管理员登录失败，email：{}，错误：", request.getEmail(), e);
            return Result.error(ResultCode.UNAUTHORIZED, "登录失败: " + e.getMessage());
        }
    }

    @Operation(summary = "查询学校列表", description = "常用于下拉选择")
    @GetMapping("/schools")
    public Result<?> listSchools(@RequestParam(required = false) String keyword) {
        log.info("查询学校列表，关键字：{}", keyword);
        List<School> schools = schoolService.listSchoolsByKeyword(keyword);
        log.info("查询学校列表成功，返回学校数量：{}", schools.size());
        return Result.success(schools);
    }
}