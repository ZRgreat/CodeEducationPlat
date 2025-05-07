package sdu.codeeducationplat.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sdu.codeeducationplat.common.Result;
import sdu.codeeducationplat.dto.identity.TeacherApplicationDTO;
import sdu.codeeducationplat.model.enums.CertificationStatus;
import sdu.codeeducationplat.model.enums.ResultCode;
import sdu.codeeducationplat.service.user.TeacherApplicationService;

@Tag(name = "管理员教师申请管理", description = "管理员对教师申请的审核接口")
@RestController
@RequestMapping("/api/admin/teacher-applications")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminTeacherApplicationController {

    private final TeacherApplicationService teacherApplicationService;

    @Operation(summary = "分页获取教师申请列表", description = "支持按关键字和状态查询")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> listApplications(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) CertificationStatus status,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("获取教师申请列表，关键字：{}，状态：{}，页码：{}，每页大小：{}", keyword, status, page, size);
        Page<TeacherApplicationDTO> result = teacherApplicationService.getApplicationPage(keyword, status, page, size);
        log.info("获取教师申请列表成功，返回申请数量：{}", result.getTotal());
        return Result.success(result);
    }

    @Operation(summary = "审核教师申请", description = "通过或拒绝教师申请")
    @PostMapping("/review")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> review(@RequestParam Long applicationId, @RequestParam boolean approve) {
        log.info("审核教师申请，applicationId：{}，approve：{}", applicationId, approve);
        try {
            teacherApplicationService.reviewApplication(applicationId, approve);
            log.info("审核教师申请成功，applicationId：{}", applicationId);
            return Result.success();
        } catch (Exception e) {
            log.error("审核教师申请失败，applicationId：{}，错误：", applicationId, e);
            return Result.error(ResultCode.SERVER_ERROR, "审核失败: " + e.getMessage());
        }
    }

    @Operation(summary = "统计待审核申请数", description = "返回 CertificationStatus 为 PENDING 的申请数量")
    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<Long> countPending() {
        long count = teacherApplicationService.countPendingApplications();
        return Result.success(count);
    }
}