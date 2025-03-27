package sdu.codeeducationplat.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sdu.codeeducationplat.common.Result;
import sdu.codeeducationplat.dto.UserQueryDTO;
import sdu.codeeducationplat.model.User;
import sdu.codeeducationplat.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/api/admin/user")
@RequiredArgsConstructor
public class UserAdminController {

    private final UserService userService;

    @Operation(summary = "获取用户列表，支持模糊搜索")
    @GetMapping("/list")
    @PreAuthorize("hasRole('ADMIN')") // 确保只有 ADMIN 角色可以访问
    public List<User> listUsers(@RequestParam(required = false) String keyword) {
        return userService.queryUsersByKeyword(keyword);
    }

    @Operation(summary = "封禁/解封用户")
    @PostMapping("/toggle-active")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<String> toggleActive(@RequestParam Long uid, @RequestParam boolean active) {
        userService.setUserActiveStatus(uid, active);
        return Result.success("操作成功");
    }
    @Operation(summary = "重置用户密码")
    @PostMapping("/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<String> resetPassword(@RequestParam Long uid) {
        userService.resetPassword(uid);
        return Result.success("操作成功，密码已重置为默认密码：123456");
    }
}