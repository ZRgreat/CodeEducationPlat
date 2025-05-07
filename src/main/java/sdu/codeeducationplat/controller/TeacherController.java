package sdu.codeeducationplat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sdu.codeeducationplat.dto.identity.TeacherProfileDTO;
import sdu.codeeducationplat.service.user.TeacherService;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping("/profile")
    public TeacherProfileDTO getTeacherProfile(Authentication authentication) {
        Long uid = (Long) authentication.getPrincipal();
        return teacherService.getTeacherProfileByUid(uid);
    }
}