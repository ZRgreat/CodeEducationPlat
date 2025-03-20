package sdu.codeeducationplat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import sdu.codeeducationplat.service.UserService;

@Tag(name = "管理员接口", description = "管理员相关操作")
@RestController
@RequestMapping("api/admin")
public class AdminController {
    @Autowired
    private UserService userService;

    @Operation(summary = "重置用户密码", description = "管理员重置指定用户的密码")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/reset-password")
    public void resetPassword(@Parameter(description = "UID", required = true) @RequestParam String uid,
                              @Parameter(description = "新密码", required = true) @RequestParam String newPassword) {
        userService.resetPassword(uid, newPassword);
    }
}