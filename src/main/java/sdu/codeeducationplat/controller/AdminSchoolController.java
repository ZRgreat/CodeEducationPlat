package sdu.codeeducationplat.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sdu.codeeducationplat.common.Result;
import sdu.codeeducationplat.dto.identity.SchoolDTO;
import sdu.codeeducationplat.model.school.School;
import sdu.codeeducationplat.model.enums.ResultCode;
import sdu.codeeducationplat.service.school.SchoolService;

@Tag(name = "管理员学校管理", description = "管理员对学校的管理接口")
@RestController
@RequestMapping("/api/admin/schools")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminSchoolController {

    private final SchoolService schoolService;

    @Operation(summary = "添加学校", description = "管理员添加新的学校信息")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> addSchool(@Valid @RequestBody SchoolDTO dto) {
        log.info("添加学校，dto：{}", dto);
        try {
            School school = schoolService.addSchool(dto);
            log.info("添加学校成功，schoolId：{}", school.getSchoolId());
            return Result.success(school);
        } catch (Exception e) {
            log.error("添加学校失败，dto：{}，错误：", dto, e);
            return Result.error(ResultCode.SERVER_ERROR, "添加学校失败: " + e.getMessage());
        }
    }

    @Operation(summary = "查询学校列表", description = "分页查询学校列表，支持模糊搜索学校名称或代码")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Result<Page<School>> getSchools(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {
        log.info("获取学校列表，关键字：{}，页码：{}，每页大小：{}", keyword, page, size);
        Page<School> result = schoolService.getSchoolPage(keyword, page, size);
        log.info("获取学校列表成功，返回学校数量：{}", result.getTotal());
        return Result.success(result);
    }

    @Operation(summary = "删除学校", description = "管理员删除指定学校")
    @DeleteMapping("/{schoolId}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> deleteSchool(@PathVariable Long schoolId) {
        log.info("删除学校，schoolId：{}", schoolId);
        try {
            schoolService.deleteSchool(schoolId);
            log.info("删除学校成功，schoolId：{}", schoolId);
            return Result.success("删除成功");
        } catch (Exception e) {
            log.error("删除学校失败，schoolId：{}，错误：", schoolId, e);
            return Result.error(ResultCode.NOT_FOUND, "学校不存在或删除失败: " + e.getMessage());
        }
    }


}