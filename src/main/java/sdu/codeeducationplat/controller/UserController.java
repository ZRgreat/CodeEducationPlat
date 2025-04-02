package sdu.codeeducationplat.controller;

import io.jsonwebtoken.JwtException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import sdu.codeeducationplat.common.Result; // 确保正确导入 Result
import sdu.codeeducationplat.dto.*;
import sdu.codeeducationplat.service.StudentService;
import sdu.codeeducationplat.service.TeacherApplicationService;
import sdu.codeeducationplat.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 用户控制器类，处理用户相关的 API 请求
 */
@Tag(name = "用户个人操作", description = "用户对自己账户的操作接口，包括获取个人信息、修改昵称、修改头像等")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final TeacherApplicationService teacherApplicationService;
    private final StudentService studentService;
    /**
     * 获取用户详情接口
     *
     * @return 用户详情，成功返回用户信息，失败抛出异常
     */
    @Operation(summary = "获取个人信息", description = "获取当前登录用户的基本信息，包括昵称、头像、邮箱等")
    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public Result<UserProfileDTO> getUserDetails(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new JwtException("用户未登录");
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof Long)) {
            throw new IllegalStateException("Principal 不是 Long 类型: " + principal.getClass());
        }
        Long uid = (Long) authentication.getPrincipal();
        UserProfileDTO dto = userService.getUserDetails(uid);
        return Result.success(dto);
    }

    /**
     * 用户修改昵称接口
     *
     * @param dto            更新用户昵称的请求体，包含新昵称
     * @param authentication 当前用户身份信息和认证状态
     * @return 返回操作信息
     */
    @Operation(summary = "修改昵称", description = "修改当前登录用户的昵称")
    @PutMapping("/me/nickname")
    @PreAuthorize("isAuthenticated() and !hasRole('ADMIN') and hasAnyRole('USER', 'STUDENT', 'TEACHER')")
    public Result<Void> updateNickname(@Valid @RequestBody UpdateNicknameDTO dto, Authentication authentication) {
        Long uid = (Long) authentication.getPrincipal();
        userService.updateNickname(uid, dto.getNickname());
        return Result.success();
    }

    /**
     * 用户修改头像的接口
     *
     * @param dto            更新用户头像的请求体，包含新昵称
     * @param authentication 当前用户身份信息和认证状态
     * @return 返回操作信息
     */
    @Operation(summary = "修改头像", description = "修改当前登录用户的头像（URL 形式）")
    @PutMapping("/me/avatar")
    @PreAuthorize("isAuthenticated() and !hasRole('ADMIN') and hasAnyRole('USER', 'STUDENT', 'TEACHER')")
    public Result<String> updateAvatar(@Valid @RequestBody UpdateAvatarDTO dto, Authentication authentication) {
        Long uid = (Long) authentication.getPrincipal();
        String avatar = userService.updateAvatar(uid, dto.getAvatar());
        return Result.success(avatar);
    }

    /**
     * 用户注销（物理删除）
     * @param authentication 当前用户身份信息和认证状态
     * @return 返回删除结果
     */
    @Operation(summary = "注销账户", description = "注销当前登录用户的账户（物理删除）")
    @DeleteMapping("/me")
    @PreAuthorize("isAuthenticated() and !hasRole('ADMIN') and hasAnyRole('USER', 'STUDENT', 'TEACHER')")
    public Result<Void> deleteAccount(Authentication authentication) {
        Long uid = (Long) authentication.getPrincipal();
        userService.deleteUser(uid);
        return Result.success();
    }

    /**
     * 用户申请成为教师
     * @param dto 用户申请所提交的信息
     * @param authentication 当前用户身份信息和认证状态
     * @return 申请结果
     */
    @Operation(summary = "申请成为教师", description = "当前用户申请成为教师")
    @PostMapping("/apply-teacher")
    @PreAuthorize("hasAnyRole('USER','STUDENT')")
    public Result<String> applyTeacher(
            @RequestBody @Valid ApplyTeacherDTO dto,
            Authentication authentication) {
        Long uid = (Long) authentication.getPrincipal();
        teacherApplicationService.applyTeacher(uid, dto);
        return Result.success("申请已提交，等待审核");
    }

    @Operation(summary = "用户绑定学校", description = "用户通过绑定码绑定学校成为学生")
    @PostMapping("/apply-student")
    @PreAuthorize("hasAnyRole('USER','TEACHER')")
    public Result<Void> applyStudent(
            @RequestBody @Valid ApplyStudentDTO dto,
            Authentication authentication) {
        Long uid = (Long) authentication.getPrincipal();
        studentService.applyStudent(uid, dto);
        return Result.success();
    }

}