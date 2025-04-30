package sdu.codeeducationplat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import sdu.codeeducationplat.dto.TeacherProfileDTO;
import sdu.codeeducationplat.model.user.Teacher;

public interface TeacherMapper extends BaseMapper<Teacher> {
    TeacherProfileDTO selectTeacherProfileByUid(Long uid);
}