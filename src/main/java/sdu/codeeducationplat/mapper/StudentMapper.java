package sdu.codeeducationplat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;
import sdu.codeeducationplat.dto.StudentBindingDTO;
import sdu.codeeducationplat.model.user.Student;

import java.util.List;

public interface StudentMapper extends BaseMapper<Student> {
    @Select("SELECT s.id, s.uid, s.school_id AS schoolId, s.student_number AS studentNumber, " +
            "s.student_name AS studentName, s.created_at AS createdAt, sc.name AS schoolName " +
            "FROM student s " +
            "LEFT JOIN school sc ON s.school_id = sc.school_id " +
            "WHERE s.uid = #{uid}")
    List<StudentBindingDTO> getStudentBindings(Long uid);
}
