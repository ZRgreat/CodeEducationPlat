package sdu.codeeducationplat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import sdu.codeeducationplat.common.Result;
import sdu.codeeducationplat.dto.LoginRequestDTO;
import sdu.codeeducationplat.model.Admin;
import sdu.codeeducationplat.model.enums.RoleEnum;
import sdu.codeeducationplat.service.AdminService;
import sdu.codeeducationplat.util.JwtUtil;


@Tag(name = "管理员接口", description = "管理员相关操作")
@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Operation(summary = "管理员登录", description = "通过邮箱和密码登录，返回 JWT Token")
    @SecurityRequirement(name = "")
    @PostMapping("/login")
    public Result<String> login(@RequestBody LoginRequestDTO request) {
        Admin admin = adminService.login(request.getEmail(), request.getPassword());
        String token = JwtUtil.generateToken(admin.getAdminId(), RoleEnum.ADMIN);
        return Result.success(token);
    }

}