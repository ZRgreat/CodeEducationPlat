package sdu.codeeducationplat.mapper.course;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import sdu.codeeducationplat.dto.classCourse.ClassDTO;
import sdu.codeeducationplat.model.course.Class;

public interface ClassMapper extends BaseMapper<Class> {
    @Select("SELECT c.*, co.title AS course_title, s.name AS school_name " +
            "FROM class c " +
            "LEFT JOIN course co ON c.course_id = co.course_id " +
            "LEFT JOIN school s ON c.school_id = s.school_id " +
            "WHERE c.class_id = #{classId}")
    ClassDTO selectClassWithDetails(Long classId);
}

