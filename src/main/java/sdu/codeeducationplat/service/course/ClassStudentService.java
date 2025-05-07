package sdu.codeeducationplat.service.course;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import sdu.codeeducationplat.mapper.course.ClassStudentMapper;
import sdu.codeeducationplat.mapper.user.StudentMapper;
import sdu.codeeducationplat.model.course.Class;
import sdu.codeeducationplat.model.course.ClassStudent;
import sdu.codeeducationplat.model.user.Student;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ClassStudentService {

    private final ClassStudentMapper classStudentMapper;
    private final ClassService classService;
    private final StudentMapper studentMapper;

    /**
     * 学生通过绑定码加入班级
     *
     * @param uid         用户ID
     * @param bindingCode 绑定码
     */
    public void joinClass(Long uid, String bindingCode) {
        Class classInfo = classService.getClassByBindingCode(bindingCode);
        if (classInfo == null) {
            throw new IllegalArgumentException("无效的绑定码");
        }

        LambdaQueryWrapper<Student> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Student::getUid, uid);
        List<Student> studentIdentities = studentMapper.selectList(queryWrapper);
        if (studentIdentities.isEmpty()) {
            throw new IllegalStateException("用户没有绑定任何学生身份");
        }

        Long classSchoolId = classInfo.getSchoolId();
        List<Long> studentSchoolIds = studentIdentities.stream()
                .map(Student::getSchoolId)
                .collect(Collectors.toList());
        if (!studentSchoolIds.contains(classSchoolId)) {
            throw new IllegalStateException("学生身份不属于班级所在学校，无法加入");
        }

        Student matchedStudent = studentIdentities.stream()
                .filter(student -> student.getSchoolId().equals(classSchoolId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("未找到匹配学校的学生身份"));

        LambdaQueryWrapper<ClassStudent> classStudentQuery = new LambdaQueryWrapper<>();
        classStudentQuery.eq(ClassStudent::getClassId, classInfo.getClassId())
                .eq(ClassStudent::getStudentId, matchedStudent.getUid());
        if (classStudentMapper.selectCount(classStudentQuery) > 0) {
            throw new IllegalStateException("学生已加入该班级");
        }

        ClassStudent classStudent = new ClassStudent();
        classStudent.setClassId(classInfo.getClassId());
        classStudent.setStudentId(matchedStudent.getUid());
        classStudentMapper.insert(classStudent);
    }
}