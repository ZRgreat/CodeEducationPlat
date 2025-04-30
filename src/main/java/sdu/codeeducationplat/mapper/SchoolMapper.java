package sdu.codeeducationplat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import sdu.codeeducationplat.model.school.School;

public interface SchoolMapper extends BaseMapper<School> {
    @Select("SELECT name FROM school WHERE school_id = #{schoolId}")
    String selectSchoolNameById(Long schoolId);
}
