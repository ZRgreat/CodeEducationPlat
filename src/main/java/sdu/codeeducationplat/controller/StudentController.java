package sdu.codeeducationplat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sdu.codeeducationplat.common.Result;
import sdu.codeeducationplat.dto.classCourse.JoinClassDTO;
import sdu.codeeducationplat.dto.classCourse.StudentCourseDTO;
import sdu.codeeducationplat.model.enums.ResultCode;
import sdu.codeeducationplat.service.course.ClassStudentService;
import sdu.codeeducationplat.service.user.StudentService;

import java.util.List;

@Tag(name = "学生操作", description = "学生相关操作接口，如加入班级")
@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class StudentController {

    private final ClassStudentService classStudentService;
    private final StudentService studentService;

    @Operation(summary = "通过绑定码加入班级", description = "学生通过绑定码加入指定班级")
    @PostMapping("/join-class")
    @PreAuthorize("hasAnyRole('USER', 'STUDENT')")
    public Result<?> joinClass(@RequestBody JoinClassDTO dto, Authentication authentication) {
        log.info("学生尝试加入班级，绑定码：{}", dto.getBindingCode());
        Long uid = (Long) authentication.getPrincipal();
        try {
            classStudentService.joinClass(uid, dto.getBindingCode());
            log.info("学生加入班级成功，uid：{}", uid);
            return Result.success("成功加入班级");
        } catch (Exception e) {
            log.error("学生加入班级失败，uid：{}，错误：{}", uid, e.getMessage());
            return Result.error(ResultCode.SERVER_ERROR, "加入班级失败: " + e.getMessage());
        }
    }

    @Operation(summary = "获取学生课程信息", description = "返回当前用户的所有学生身份及其课程信息")
    @GetMapping("/courses")
    @PreAuthorize("hasAnyRole('USER', 'STUDENT')")
    public Result<List<StudentCourseDTO>> getStudentCourses(Authentication authentication) {
        log.info("获取学生课程信息");
        Long uid = (Long) authentication.getPrincipal();
        try {
            List<StudentCourseDTO> courses = studentService.getStudentCourses(uid);
            log.info("获取学生课程信息成功，uid：{}", uid);
            return Result.success(courses);
        } catch (Exception e) {
            log.error("获取学生课程信息失败，uid：{}，错误：{}", uid, e.getMessage());
            return Result.error(ResultCode.SERVER_ERROR, "获取课程信息失败: " + e.getMessage());
        }
    }

}