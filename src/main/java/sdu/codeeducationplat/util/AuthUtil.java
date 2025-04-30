package sdu.codeeducationplat.util;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import sdu.codeeducationplat.common.Result;
import sdu.codeeducationplat.model.enums.ResultCode;
import sdu.codeeducationplat.model.user.Teacher;
import sdu.codeeducationplat.service.TeacherService;

@Slf4j
public class AuthUtil {

    public static Result<?> getAuthenticatedTeacher(Authentication authentication, TeacherService teacherService) {
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("User not authenticated");
            return Result.error(ResultCode.UNAUTHORIZED, "User not authenticated");
        }

        Object principal = authentication.getPrincipal();
        if (!(principal instanceof Long)) {
            log.warn("Principal is not of type Long: {}", principal.getClass());
            return Result.error(ResultCode.SERVER_ERROR, "Invalid principal type: " + principal.getClass());
        }

        Long uid = (Long) principal;
        Teacher teacher = teacherService.getOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getUid, uid));
        if (teacher == null) {
            log.warn("Teacher record not found, uid: {}", uid);
            return Result.error(ResultCode.NOT_FOUND, "Teacher record not found");
        }

        return Result.success(teacher);
    }
}