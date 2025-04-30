package sdu.codeeducationplat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sdu.codeeducationplat.common.Result;
import sdu.codeeducationplat.model.enums.ResultCode;
import sdu.codeeducationplat.model.school.Class;
import sdu.codeeducationplat.model.user.Teacher;
import sdu.codeeducationplat.service.ClassService;
import sdu.codeeducationplat.service.TeacherService;

import java.util.List;

@Tag(name = "教师班级管理", description = "教师对班级的操作接口")
@RestController
@RequestMapping("/api/teacher/classes")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class TeacherClassController {

    private final ClassService classService;
    private final TeacherService teacherService;

    @Operation(summary = "创建班级", description = "教师创建新班级")
    @PostMapping
    @PreAuthorize("hasRole('TEACHER')")
    public Result<?> createClass(@RequestParam String className, Authentication authentication) {
        log.info("创建班级，className: {}", className);
        Long teacherId = teacherService.getTeacherIdByAuthentication(authentication);
        Class newClass = classService.createClass(teacherId, className);
        log.info("创建班级成功，classId: {}", newClass.getClassId());
        return Result.success(newClass);
    }

    @Operation(summary = "获取班级列表", description = "获取教师创建的班级列表")
    @GetMapping
    @PreAuthorize("hasRole('TEACHER')")
    public Result<?> listClasses(Authentication authentication) {
        log.info("获取班级列表");
        Long teacherId = teacherService.getTeacherIdByAuthentication(authentication);
        List<Class> classes = classService.list(
                new LambdaQueryWrapper<Class>().eq(Class::getTeacherId, teacherId)
        );
        log.info("获取班级列表成功，返回班级数量: {}", classes.size());
        return Result.success(classes);
    }

    private Long getUid(Authentication authentication) {
        return (Long) authentication.getPrincipal();
    }

}