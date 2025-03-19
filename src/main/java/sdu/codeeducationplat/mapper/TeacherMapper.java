package sdu.codeeducationplat.mapper;

import java.util.List;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import sdu.codeeducationplat.model.Teacher;
import org.apache.ibatis.annotations.*;
import sdu.codeeducationplat.model.enums.CertificationStatus;

@Mapper
public interface TeacherMapper extends BaseMapper<Teacher> {
    // 通过教师职工号查找
    @Select("SELECT * FROM teacher WHERE teacher_num = #{teacher_num}")
    List<Teacher> selectTeacherListByTeacherId(String teacherID);

    // 通过教师姓名查找
    @Select("SELECT * FROM teacher WHERE teacher_name " +
            "LIKE CONCAT('%', #{teacher_name}, '%') " +
            "ORDER BY teacher_num")
    List<Teacher> selectTeacherListByTeacherName(String teacherName);

    // 通过姓名或者职工号查找
    @Select("SELECT * FROM teacher WHERE " +
            "teacher_name LIKE CONCAT('%', #{teacher_name}, '%') OR " +
            "teacher_num LIKE CONCAT('%', #{teacher_num},'%')" +
            "ORDER BY teacher_num")
    List<Teacher> selectTeacherListByNumName(String numName);

    // 通过审核状态查找
    @Select("SELECT * FROM teacher WHERE certified = #{certified} ORDER BY teacher_num")
    List<Teacher> selectTeacherListByStatus(@Param("certified") CertificationStatus certified);


}
