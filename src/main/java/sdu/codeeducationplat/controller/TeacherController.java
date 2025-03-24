package sdu.codeeducationplat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sdu.codeeducationplat.service.TeacherService;

@RestController
@RequestMapping("/api/teachers")
public class TeacherController {

    @Autowired
    private TeacherService teacherService;

    @PostMapping("/approve-application")
    public ResponseEntity<String> approveApplication(@RequestParam Long applicationId) {
        teacherService.approveApplication(applicationId);
        return ResponseEntity.ok("申请已通过，教师创建成功");
    }

    @PostMapping("/reject-application")
    public ResponseEntity<String> rejectApplication(@RequestParam Long applicationId) {
        teacherService.rejectApplication(applicationId);
        return ResponseEntity.ok("申请已拒绝");
    }
}