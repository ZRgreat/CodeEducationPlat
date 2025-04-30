package sdu.codeeducationplat.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.codeeducationplat.dto.ApplyStudentDTO;
import sdu.codeeducationplat.dto.StudentBindingDTO;
import sdu.codeeducationplat.mapper.StudentMapper;
import sdu.codeeducationplat.model.user.Student;
import sdu.codeeducationplat.model.user.Teacher;
import sdu.codeeducationplat.model.enums.RoleEnum;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService extends ServiceImpl<StudentMapper, Student> {

    private final TeacherService teacherService;
    private final UserRoleService userRoleService;

    @Transactional(rollbackFor = Exception.class)
    public void applyStudent(Long uid, ApplyStudentDTO dto) {
        // 检查绑定码是否存在
        Teacher teacher = teacherService.findByBindCode(dto.getBindCode());
        if (teacher == null) {
            throw new RuntimeException("绑定码无效");
        }

        // 检查教师的学校与学生选择的学校是否一致
        if (!teacher.getSchoolId().equals(dto.getSchoolId())) {
            throw new RuntimeException("绑定码对应的学校与所选学校不一致");
        }

        // 检查学生是否已绑定该学校
        if (exists(new QueryWrapper<Student>().eq("uid", uid).eq("school_id", dto.getSchoolId()))) {
            throw new RuntimeException("您已绑定该学校");
        }

        // 检查学号是否在该学校内唯一
        if (exists(new QueryWrapper<Student>().eq("school_id", dto.getSchoolId()).eq("student_number", dto.getStudentNumber()))) {
            throw new RuntimeException("该学号在学校内已被使用");
        }

        // 插入学生记录
        Student student = new Student();
        student.setUid(uid);
        student.setSchoolId(dto.getSchoolId());
        student.setStudentNumber(dto.getStudentNumber());
        student.setStudentName(dto.getStudentName());
        save(student);

        // 更新用户角色为 STUDENT
        userRoleService.saveOrUpdateUserRole(uid, RoleEnum.STUDENT);
    }

    public List<StudentBindingDTO> getStudentBindings(Long uid) {
        return getBaseMapper().getStudentBindings(uid);
    }

}