package sdu.codeeducationplat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sdu.codeeducationplat.dto.TeacherApplicationDTO;
import sdu.codeeducationplat.service.TeacherApplicationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/teacher-applications")
public class TeacherApplicationController {

    @Autowired
    private TeacherApplicationService teacherApplicationService;

    @PostMapping("/submit")
    public ResponseEntity<String> submitApplication(@Valid @RequestBody TeacherApplicationDTO dto) {
        teacherApplicationService.submitApplication(dto);
        return ResponseEntity.ok("申请提交成功");
    }
}