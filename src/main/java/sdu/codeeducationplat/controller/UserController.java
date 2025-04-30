package sdu.codeeducationplat.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sdu.codeeducationplat.common.Result;
import sdu.codeeducationplat.dto.*;
import sdu.codeeducationplat.model.enums.ResultCode;
import sdu.codeeducationplat.service.StudentService;
import sdu.codeeducationplat.service.TeacherApplicationService;
import sdu.codeeducationplat.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "用户个人操作", description = "用户对自己账户的操作接口，包括获取个人信息、修改昵称、修改头像等")
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class UserController {

    private final UserService userService;
    private final TeacherApplicationService teacherApplicationService;
    private final StudentService studentService;

    @Operation(summary = "获取个人信息", description = "获取当前登录用户的基本信息，包括昵称、头像、邮箱等")
    @GetMapping("/getProfile")
    @PreAuthorize("isAuthenticated()")
    public Result<?> getUserDetails(Authentication authentication) {
        log.info("获取个人信息");
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("用户未登录");
            return Result.error(ResultCode.UNAUTHORIZED, "用户未登录");
        }
        Object principal = authentication.getPrincipal();
        if (!(principal instanceof Long)) {
            log.warn("Principal 不是 Long 类型: {}", principal.getClass());
            return Result.error(ResultCode.SERVER_ERROR, "Principal 不是 Long 类型: " + principal.getClass());
        }
        Long uid = (Long) authentication.getPrincipal();
        try {
            UserProfileDTO dto = userService.getUserDetails(uid);
            log.info("获取个人信息成功，uid：{}", uid);
            return Result.success(dto);
        } catch (Exception e) {
            log.error("获取个人信息失败，uid：{}，错误：", uid, e);
            return Result.error(ResultCode.SERVER_ERROR, "获取用户信息失败: " + e.getMessage());
        }
    }

    @Operation(summary = "修改昵称", description = "修改当前登录用户的昵称")
    @PutMapping("/nickname")
    @PreAuthorize("isAuthenticated() and !hasRole('ADMIN') and hasAnyRole('USER', 'STUDENT', 'TEACHER')")
    public Result<?> updateNickname(@Valid @RequestBody UpdateNicknameDTO dto, Authentication authentication) {
        log.info("修改昵称，dto：{}", dto);
        Long uid = (Long) authentication.getPrincipal();
        try {
            userService.updateNickname(uid, dto.getNickname());
            log.info("修改昵称成功，uid：{}", uid);
            return Result.success();
        } catch (Exception e) {
            log.error("修改昵称失败，uid：{}，错误：", uid, e);
            return Result.error(ResultCode.SERVER_ERROR, "修改昵称失败: " + e.getMessage());
        }
    }

    @Operation(summary = "修改头像", description = "修改当前登录用户的头像（URL 形式）")
    @PutMapping("/avatar")
    @PreAuthorize("isAuthenticated() and !hasRole('ADMIN') and hasAnyRole('USER', 'STUDENT', 'TEACHER')")
    public Result<?> updateAvatar(@Valid @RequestBody UpdateAvatarDTO dto, Authentication authentication) {
        log.info("修改头像，dto：{}", dto);
        Long uid = (Long) authentication.getPrincipal();
        try {
            String avatar = userService.updateAvatar(uid, dto.getAvatar());
            log.info("修改头像成功，uid：{}", uid);
            return Result.success(avatar);
        } catch (Exception e) {
            log.error("修改头像失败，uid：{}，错误：", uid, e);
            return Result.error(ResultCode.SERVER_ERROR, "修改头像失败: " + e.getMessage());
        }
    }

    @Operation(summary = "注销账户", description = "注销当前登录用户的账户（物理删除）")
    @DeleteMapping("/account")
    @PreAuthorize("isAuthenticated() and !hasRole('ADMIN') and hasAnyRole('USER', 'STUDENT', 'TEACHER')")
    public Result<?> deleteAccount(Authentication authentication) {
        log.info("注销账户");
        Long uid = (Long) authentication.getPrincipal();
        try {
            userService.deleteUser(uid);
            log.info("注销账户成功，uid：{}", uid);
            return Result.success();
        } catch (Exception e) {
            log.error("注销账户失败，uid：{}，错误：", uid, e);
            return Result.error(ResultCode.SERVER_ERROR, "注销账户失败: " + e.getMessage());
        }
    }

    @Operation(summary = "申请成为教师", description = "当前用户申请成为教师")
    @PostMapping("/apply-teacher")
    @PreAuthorize("hasAnyRole('USER','STUDENT')")
    public Result<?> applyTeacher(@RequestBody @Valid ApplyTeacherDTO dto, Authentication authentication) {
        log.info("申请成为教师，dto：{}", dto);
        Long uid = (Long) authentication.getPrincipal();
        try {
            teacherApplicationService.applyTeacher(uid, dto);
            log.info("申请成为教师成功，uid：{}", uid);
            return Result.success("申请已提交，等待审核");
        } catch (Exception e) {
            log.error("申请成为教师失败，uid：{}，错误：", uid, e);
            return Result.error(ResultCode.SERVER_ERROR, "申请失败: " + e.getMessage());
        }
    }

    @Operation(summary = "用户绑定学校", description = "用户通过绑定码绑定学校成为学生")
    @PostMapping("/apply-student")
    @PreAuthorize("hasAnyRole('USER','TEACHER')")
    public Result<?> applyStudent(@RequestBody @Valid ApplyStudentDTO dto, Authentication authentication) {
        log.info("用户绑定学校，dto：{}", dto);
        Long uid = (Long) authentication.getPrincipal();
        try {
            studentService.applyStudent(uid, dto);
            log.info("用户绑定学校成功，uid：{}", uid);
            return Result.success();
        } catch (Exception e) {
            log.error("用户绑定学校失败，uid：{}，错误：", uid, e);
            return Result.error(ResultCode.SERVER_ERROR, "绑定学校失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取当前用户的所有学生绑定信息")
    @GetMapping("/bindings")
    @PreAuthorize("hasAnyRole('USER','STUDENT', 'TEACHER')")
    public Result<?> getStudentBindings(Authentication authentication) {
        Long uid = (Long) authentication.getPrincipal();
        return Result.success(studentService.getStudentBindings(uid));
    }

    @Operation(summary = "获取当前用户的教师申请状态")
    @GetMapping("/teacher-application")
    @PreAuthorize("hasAnyRole('USER','STUDENT')")
    public Result<?> getTeacherApplication(Authentication authentication) {
        Long uid = (Long) authentication.getPrincipal();
        TeacherApplicationDTO dto = teacherApplicationService.getLatestApplication(uid);
        return Result.success(dto);
    }

    @Operation(summary = "修改教师申请信息", description = "仅当申请状态为 PENDING 或 REJECTED 时可修改")
    @PutMapping("/teacher-application")
    @PreAuthorize("isAuthenticated() and !hasRole('ADMIN') and hasAnyRole('USER', 'STUDENT')")
    public Result<?> updateTeacherApplication(@Valid @RequestBody TeacherApplicationDTO dto, Authentication authentication) {
        Long uid = (Long) authentication.getPrincipal();
        try {
            teacherApplicationService.updateTeacherApplication(uid, dto);
            return Result.success();
        } catch (Exception e) {
            return Result.error(ResultCode.SERVER_ERROR, "教师申请信息修改失败：" + e.getMessage());
        }
    }

}