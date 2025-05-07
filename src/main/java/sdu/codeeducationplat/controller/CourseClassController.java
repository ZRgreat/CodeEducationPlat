package sdu.codeeducationplat.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sdu.codeeducationplat.common.Result;
import sdu.codeeducationplat.dto.classCourse.ClassDTO;
import sdu.codeeducationplat.dto.classCourse.CourseDTO;
import sdu.codeeducationplat.dto.identity.StudentDTO;
import sdu.codeeducationplat.dto.identity.TeacherDTO;
import sdu.codeeducationplat.model.user.Teacher;
import sdu.codeeducationplat.service.course.ClassService;
import sdu.codeeducationplat.service.course.CourseService;
import sdu.codeeducationplat.service.user.TeacherService;

import java.util.List;

@Tag(name = "课程管理", description = "教师的课程管理接口")
@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class CourseClassController {

    private final CourseService courseService;
    private final ClassService classService;
    private final TeacherService teacherService;

    @Operation(summary = "创建课程")
    @PostMapping
    public Result<CourseDTO> createCourse(@Valid @RequestBody CourseDTO dto, Authentication authentication) {
        Long teacherUid = (Long) authentication.getPrincipal();
        dto.setTeacherUid(teacherUid);
        // 查询教师信息以获取 schoolId，使用 uid 查询
        Teacher teacher = teacherService.getTeacherByUid(teacherUid);
        if (teacher.getSchoolId() == null) {
            throw new IllegalStateException("教师的学校ID为空，uid: " + teacherUid);
        }
        dto.setSchoolId(teacher.getSchoolId());

        CourseDTO result = courseService.createCourse(dto);
        return Result.success(result);
    }

    @Operation(summary = "更新课程")
    @PutMapping("/{id}")
    public Result<CourseDTO> updateCourse(@PathVariable Long id, @Valid @RequestBody CourseDTO dto, Authentication authentication) {
        Long teacherUid = (Long) authentication.getPrincipal();
        dto.setTeacherUid(teacherUid);

        // 查询教师信息以获取 schoolId，使用 uid 查询
        Teacher teacher = teacherService.getTeacherByUid(teacherUid);
        if (teacher.getSchoolId() == null) {
            throw new IllegalStateException("教师的学校ID为空，uid: " + teacherUid);
        }
        dto.setSchoolId(teacher.getSchoolId());

        CourseDTO result = courseService.updateCourse(id, dto);
        return Result.success(result);
    }

    @Operation(summary = "删除课程")
    @DeleteMapping("/{id}")
    public Result<String> deleteCourse(@PathVariable Long id, Authentication authentication) {
        Long teacherUid = (Long) authentication.getPrincipal();
        CourseDTO course = courseService.getDtoById(id);
        if (course == null || !course.getTeacherUid().equals(teacherUid)) {
            throw new IllegalStateException("无权操作此课程");
        }
        courseService.deleteCourse(id);
        return Result.success("删除成功");
    }

    @Operation(summary = "获取课程列表")
    @GetMapping
    public Result<List<CourseDTO>> listCourses(Authentication authentication) {
        Long teacherUid = (Long) authentication.getPrincipal();
        List<CourseDTO> courses = courseService.listCourses(teacherUid);
        return Result.success(courses);
    }

    @GetMapping("/{courseId}/classes")
    @PreAuthorize("@courseService.getDtoById(#courseId).getTeacherUid().equals(authentication.principal)")
    public Result<List<ClassDTO>> getClassesByCourseId(@PathVariable Long courseId, Authentication authentication) {
        List<ClassDTO> classes = courseService.getClassesByCourseId(courseId);
        return Result.success(classes);
    }

    @Operation(summary = "创建班级")
    @PostMapping("/classes")
    public Result<ClassDTO> createClass(@Valid @RequestBody ClassDTO dto, Authentication authentication) {
        Long teacherUid = (Long) authentication.getPrincipal();
        dto.setTeacherUid(teacherUid);
        ClassDTO result = classService.createClass(dto);
        return Result.success(result);
    }

    @Operation(summary = "更新班级")
    @PutMapping("/classes/{id}")
    public Result<ClassDTO> updateClass(@PathVariable Long id, @Valid @RequestBody ClassDTO dto, Authentication authentication) {
        Long teacherUid = (Long) authentication.getPrincipal();
        dto.setTeacherUid(teacherUid);
        ClassDTO result = classService.updateClass(id, dto);
        return Result.success(result);
    }

    @Operation(summary = "删除班级")
    @DeleteMapping("/classes/{id}")
    public Result<String> deleteClass(@PathVariable Long id, Authentication authentication) {
        Long teacherUid = (Long) authentication.getPrincipal();
        ClassDTO cls = classService.getClassDtoById(id);
        if (cls == null || !cls.getTeacherUid().equals(teacherUid)) {
            throw new IllegalStateException("无权操作此班级");
        }
        classService.deleteClass(id);
        return Result.success("删除成功");
    }

    @GetMapping("/classes/{classId}")
    @PreAuthorize("@classService.hasClassPermission(#classId, authentication.principal)")
    public Result<ClassDTO> getClassDetail(@PathVariable Long classId, Authentication authentication) {
        ClassDTO cls = classService.getClassDtoById(classId);
        return Result.success(cls);
    }

    @GetMapping("/classes/{classId}/teacher")
    @PreAuthorize("@classService.hasClassPermission(#classId, authentication.principal)")
    public Result<TeacherDTO> getTeacherInfo(@PathVariable Long classId, Authentication authentication) {
        TeacherDTO teacher = classService.getTeacherInfo(classId);
        return Result.success(teacher);
    }

    @GetMapping("/classes/{classId}/students")
    @PreAuthorize("@classService.hasClassPermission(#classId, authentication.principal)")
    public Result<Page<StudentDTO>> getClassStudents(
            @PathVariable Long classId,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            Authentication authentication) {
        Page<StudentDTO> students = classService.getClassStudents(classId, page, pageSize);
        return Result.success(students);
    }

}