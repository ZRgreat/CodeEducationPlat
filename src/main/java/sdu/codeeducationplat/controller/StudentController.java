package sdu.codeeducationplat.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sdu.codeeducationplat.common.Result;
import sdu.codeeducationplat.model.enums.ResultCode;
import sdu.codeeducationplat.service.ClassStudentService;
import sdu.codeeducationplat.util.JwtUtil;

@Tag(name = "学生操作", description = "学生相关操作接口，如加入班级")
@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class StudentController {

    private final ClassStudentService classStudentService;

    @Operation(summary = "学生加入班级", description = "学生通过班级绑定码加入班级")
    @PostMapping("/join-class")
    @PreAuthorize("hasRole('STUDENT')")
    public Result<?> joinClass(@RequestParam String bindingCode, Authentication authentication) {
        log.info("学生加入班级，bindingCode：{}", bindingCode);
        try {
            Long studentId = JwtUtil.getUidFromToken(authentication.getCredentials().toString());
            classStudentService.joinClass(studentId, bindingCode);
            log.info("学生加入班级成功，studentId：{}", studentId);
            return Result.success();
        } catch (Exception e) {
            log.error("学生加入班级失败，bindingCode：{}，错误：", bindingCode, e);
            return Result.error(ResultCode.SERVER_ERROR, "加入班级失败: " + e.getMessage());
        }
    }

}