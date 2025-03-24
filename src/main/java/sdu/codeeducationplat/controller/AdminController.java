package sdu.codeeducationplat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sdu.codeeducationplat.common.Result;
import sdu.codeeducationplat.dto.AdminLoginDTO;
import sdu.codeeducationplat.model.Admin;
import sdu.codeeducationplat.model.User;
import sdu.codeeducationplat.service.AdminService;
import sdu.codeeducationplat.service.UserService;

import java.util.List;

@Tag(name = "管理员接口", description = "管理员相关操作")
@RestController
@RequestMapping("api/admin")
public class AdminController {
    @Autowired
    private UserService userService;


    @Autowired
    private AdminService adminService;

    @PostMapping("/login")
    public ResponseEntity<Admin> login(@RequestBody AdminLoginDTO dto) {
        Admin admin = adminService.login(dto.getUsername(), dto.getPassword());
        return ResponseEntity.ok(admin);
    }
    @Operation(summary = "重置用户密码", description = "管理员重置指定用户的密码")
    @PostMapping("/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> resetPassword(
            @Parameter(description = "UID", required = true) @RequestParam String uid,
            @Parameter(description = "新密码", required = true) @RequestParam String newPassword) {
        userService.resetPassword(uid, newPassword);
        return Result.success();
    }

    @Operation(summary = "获取所有用户", description = "管理员获取所有用户列表")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public Result<List<User>> getAllUsers() {
        List<User> users = userService.list();
        return Result.success(users);
    }

    @Operation(summary = "禁用/启用用户", description = "管理员禁用或启用指定用户")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/toggle-active")
    public Result<Void> toggleUserActive(
            @Parameter(description = "UID", required = true) @RequestParam String uid,
            @Parameter(description = "是否启用", required = true) @RequestParam boolean isActive) {
        User user = userService.getById(uid);
        if (user == null) {
            return Result.error(404, "用户不存在");
        }
        user.setIsActive(isActive);
        userService.updateById(user);
        return Result.success();
    }

}