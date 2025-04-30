package sdu.codeeducationplat.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sdu.codeeducationplat.mapper.ClassStudentMapper;
import sdu.codeeducationplat.model.school.Class;
import sdu.codeeducationplat.model.course.ClassStudent;
import sdu.codeeducationplat.model.user.Student;

@Service
@RequiredArgsConstructor
public class ClassStudentService extends ServiceImpl<ClassStudentMapper, ClassStudent> {

    private final ClassService classService;
    private final StudentService studentService;

    /**
     * 学生加入班级
     *
     * @param studentId   学生ID
     * @param bindingCode 班级绑定码
     */
    public void joinClass(Long studentId, String bindingCode) {
        // 验证学生是否存在
        Student student = studentService.getById(studentId);
        if (student == null) {
            throw new RuntimeException("学生不存在");
        }

        // 验证班级绑定码
        Class classEntity = classService.findByBindingCode(bindingCode);
        if (classEntity == null) {
            throw new RuntimeException("班级绑定码无效");
        }

        // 验证学生是否已经加入该班级
        boolean alreadyJoined = this.exists(
                new LambdaQueryWrapper<ClassStudent>()
                        .eq(ClassStudent::getClassId, classEntity.getClassId())
                        .eq(ClassStudent::getStudentId, studentId)
        );
        if (alreadyJoined) {
            throw new RuntimeException("学生已加入该班级");
        }

        // 验证学校是否匹配
        if (!student.getSchoolId().equals(classEntity.getSchoolId())) {
            throw new RuntimeException("学生与班级不在同一学校");
        }

        // 创建班级-学生关联
        ClassStudent classStudent = new ClassStudent();
        classStudent.setClassId(classEntity.getClassId());
        classStudent.setStudentId(studentId);
        this.save(classStudent);
    }
}