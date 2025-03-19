package sdu.codeeducationplat.controller;

import io.swagger.v3.oas.annotations.Operation; // 替换为 OpenAPI 3 注解
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sdu.codeeducationplat.model.Teacher;
import sdu.codeeducationplat.model.enums.CertificationStatus;
import sdu.codeeducationplat.service.TeacherService;

import java.util.List;

@Tag(name = "Teacher API", description = "教师相关操作")
@RestController
@RequestMapping("/teachers")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    @Operation(summary ="添加教师（仅管理员）")
    @PostMapping
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> addTeacher(@RequestBody Teacher teacher) {
        teacher.setCertified(CertificationStatus.PENDING);
        teacherService.addTeacher(teacher);
        return ResponseEntity.ok("教师添加成功");
    }

    // 根据 ID 查询教师
    @Operation(summary ="根据 ID 查询教师")
    @GetMapping("/{id}")
    public ResponseEntity<Teacher> getTeacherById(@PathVariable Long id) {
        Teacher teacher = teacherService.getTeacherById(id);
        return ResponseEntity.ok(teacher);
    }

    // 根据姓名查询教师
    @Operation(summary ="根据姓名查询教师")
    @GetMapping("/by-name")
    public ResponseEntity<List<Teacher>> getTeachersByName(@RequestParam String name) {
        List<Teacher> teachers = teacherService.getTeachersByName(name);
        return ResponseEntity.ok(teachers);
    }

    // 根据认证状态查询教师
    @Operation(summary ="根据认证状态查询教师")
    @GetMapping("/by-certified")
    public ResponseEntity<List<Teacher>> getTeachersByCertified(
            @RequestParam("certified") CertificationStatus certified) {
        List<Teacher> teachers = teacherService.getTeachersByCertified(certified);
        return ResponseEntity.ok(teachers);
    }

    // 删除教师（仅管理员）
    @Operation(summary ="删除教师（仅管理员）")
    @DeleteMapping("/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteTeacherById(@PathVariable Long id) {
        teacherService.deleteTeacherById(id);
        return ResponseEntity.ok("教师删除成功");
    }


}
