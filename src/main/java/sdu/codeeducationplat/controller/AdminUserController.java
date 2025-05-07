package sdu.codeeducationplat.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sdu.codeeducationplat.common.Result;
import sdu.codeeducationplat.model.user.User;
import sdu.codeeducationplat.model.enums.ResultCode;
import sdu.codeeducationplat.service.user.UserService;

@Tag(name = "管理员用户管理", description = "管理员对用户的管理接口")
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminUserController {

    private final UserService userService;

    @Operation(summary = "查询用户列表", description = "获取用户列表，支持模糊搜索用户昵称或邮箱")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Result<Page<User>> listUsers(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size) {
        log.info("查询用户列表，关键字：{}，页码：{}，每页大小：{}", keyword, page, size);
        Page<User> result = userService.getUserPage(keyword, page, size);
        log.info("查询用户列表成功，返回用户数量：{}", result.getTotal());
        return Result.success(result);
    }

    @Operation(summary = "封禁或解封用户", description = "管理员封禁或解封指定用户账户")
    @PostMapping("/{uid}/active")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> toggleActive(@PathVariable Long uid, @RequestParam boolean active) {
        log.info("封禁/解封用户，uid：{}，active：{}", uid, active);
        try {
            userService.setUserActiveStatus(uid, active);
            log.info("封禁/解封用户成功，uid：{}", uid);
            return Result.success("操作成功");
        } catch (Exception e) {
            log.error("封禁/解封用户失败，uid：{}，错误：", uid, e);
            return Result.error(ResultCode.SERVER_ERROR, "操作失败: " + e.getMessage());
        }
    }

    @Operation(summary = "重置用户密码", description = "管理员重置指定用户的密码为默认值")
    @PostMapping("/{uid}/password")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> resetPassword(@PathVariable Long uid) {
        log.info("重置用户密码，uid：{}", uid);
        try {
            userService.resetPassword(uid);
            log.info("重置用户密码成功，uid：{}", uid);
            return Result.success("操作成功，密码已重置为默认密码：123456");
        } catch (Exception e) {
            log.error("重置用户密码失败，uid：{}，错误：", uid, e);
            return Result.error(ResultCode.SERVER_ERROR, "操作失败: " + e.getMessage());
        }
    }


}