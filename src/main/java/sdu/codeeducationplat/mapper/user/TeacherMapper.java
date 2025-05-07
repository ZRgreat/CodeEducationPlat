package sdu.codeeducationplat.mapper.user;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import sdu.codeeducationplat.dto.identity.TeacherProfileDTO;
import sdu.codeeducationplat.model.user.Teacher;

public interface TeacherMapper extends BaseMapper<Teacher> {
    TeacherProfileDTO selectTeacherProfileByUid(Long uid);
}