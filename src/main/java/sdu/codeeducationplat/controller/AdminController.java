package sdu.codeeducationplat.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sdu.codeeducationplat.common.Result;
import sdu.codeeducationplat.dto.SchoolDTO;
import sdu.codeeducationplat.dto.TeacherApplicationDTO;
import sdu.codeeducationplat.model.School;
import sdu.codeeducationplat.model.User;
import sdu.codeeducationplat.model.enums.CertificationStatus;
import sdu.codeeducationplat.service.SchoolService;
import sdu.codeeducationplat.service.TeacherApplicationService;
import sdu.codeeducationplat.service.UserService;

import java.util.List;

@Tag(name = "管理员管理操作", description = "管理员对用户和学校的管理接口，包括用户管理、学校管理等")
@RestController
@RequestMapping("/api/admins")
@RequiredArgsConstructor
public class AdminController {

    //管理员的用户管理
    private final UserService userService;

    @Operation(summary = "查询用户列表", description = "获取用户列表，支持模糊搜索用户昵称或邮箱")
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')") // 确保只有 ADMIN 角色可以访问
    public List<User> listUsers(@RequestParam(required = false) String keyword) {
        return userService.queryUsersByKeyword(keyword);
    }

    @Operation(summary = "封禁或解封用户", description = "管理员封禁或解封指定用户账户")
    @PostMapping("/users/{uid}/active")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<String> toggleActive(@PathVariable Long uid, @RequestParam boolean active) {
        userService.setUserActiveStatus(uid, active);
        return Result.success("操作成功");
    }
    @Operation(summary = "重置用户密码", description = "管理员重置指定用户的密码为默认值")
    @PostMapping("/users/{uid}/password")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<String> resetPassword(@PathVariable Long uid) {
        userService.resetPassword(uid);
        return Result.success("操作成功，密码已重置为默认密码：123456");
    }

    //管理员的学校管理
    private  final SchoolService schoolService;

    @Operation(summary = "添加学校", description = "管理员添加新的学校信息")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/schools")
    public Result<School> addSchool(@Valid @RequestBody SchoolDTO dto) {
        School school = schoolService.addSchool(dto);
        return Result.success(school);
    }

    @Operation(summary = "查询学校列表", description = "分页查询学校列表，支持模糊搜索学校名称或代码")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/schools")
    public Result<Page<School>> listSchools(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        Page<School> schools = schoolService.searchSchools(keyword, page, size);
        return Result.success(schools);
    }

    @Operation(summary = "删除学校", description = "管理员删除指定学校")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/schools/{schoolId}")
    public Result<String> deleteSchool(@PathVariable Long schoolId) {
        schoolService.deleteSchool(schoolId);
        return Result.success("删除成功");
    }

    //教师申请管理
    private final TeacherApplicationService teacherApplicationService;

    @Operation(summary = "分页获取教师申请列表")
    @GetMapping("/teacher-application/list")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Page<TeacherApplicationDTO>> listApplications(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) CertificationStatus status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        Page<TeacherApplicationDTO> result = teacherApplicationService.getApplicationPage(keyword, status, page, size);
        return Result.success(result);
    }

    @Operation(summary = "审核教师申请")
    @PostMapping("/teacher-application/review")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Void> review(@RequestParam Long applicationId, @RequestParam boolean approve) {
        teacherApplicationService.reviewApplication(applicationId, approve);
        return Result.success();
    }
}