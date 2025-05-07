package sdu.codeeducationplat.service.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.codeeducationplat.dto.identity.ApplyStudentDTO;
import sdu.codeeducationplat.dto.identity.StudentBindingDTO;
import sdu.codeeducationplat.dto.classCourse.StudentCourseDTO;
import sdu.codeeducationplat.mapper.course.ClassStudentMapper;
import sdu.codeeducationplat.mapper.user.StudentMapper;
import sdu.codeeducationplat.model.course.Class;
import sdu.codeeducationplat.model.course.ClassStudent;
import sdu.codeeducationplat.model.course.Course;
import sdu.codeeducationplat.model.user.Student;
import sdu.codeeducationplat.model.user.Teacher;
import sdu.codeeducationplat.model.enums.RoleEnum;
import sdu.codeeducationplat.service.school.SchoolService;
import sdu.codeeducationplat.service.course.ClassService;
import sdu.codeeducationplat.service.course.CourseService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService extends ServiceImpl<StudentMapper, Student> {

    private final TeacherService teacherService;
    private final UserRoleService userRoleService;
    private final StudentMapper studentMapper;
    private final SchoolService schoolService;
    private final CourseService courseService;
    private final ClassService classService;
    private final ClassStudentMapper classStudentMapper;

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

    public List<StudentCourseDTO> getStudentCourses(Long uid) {
        List<StudentCourseDTO> result = new ArrayList<>();

        // 1. 查询用户的所有学生身份
        LambdaQueryWrapper<Student> studentQuery = new LambdaQueryWrapper<>();
        studentQuery.eq(Student::getUid, uid);
        List<Student> students = studentMapper.selectList(studentQuery);

        // 2. 为每个学生身份构建课程信息
        for (Student student : students) {
            StudentCourseDTO dto = new StudentCourseDTO();
            dto.setStudentId(student.getUid());
            dto.setStudentName(student.getStudentName());
            dto.setStudentNumber(student.getStudentNumber());
            dto.setSchoolName(schoolService.getSchoolNameById(student.getSchoolId()));

            // 3. 查询学生加入的班级
            LambdaQueryWrapper<ClassStudent> classStudentQuery = new LambdaQueryWrapper<>();
            classStudentQuery.eq(ClassStudent::getStudentId, student.getUid());
            List<ClassStudent> classStudents = classStudentMapper.selectList(classStudentQuery);

            // 4. 获取班级、课程和教师信息
            List<StudentCourseDTO.CourseInfo> courses = new ArrayList<>();
            for (ClassStudent cs : classStudents) {
                Class courseClass = classService.getClassById(cs.getClassId());
                if (courseClass == null) continue;

                Course course = courseService.getCourseById(courseClass.getCourseId());
                Teacher teacher = teacherService.getTeacherByUid(courseClass.getTeacherUid());

                StudentCourseDTO.CourseInfo courseInfo = new StudentCourseDTO.CourseInfo();
                courseInfo.setClassId(courseClass.getClassId());
                courseInfo.setClassName(courseClass.getName());
                courseInfo.setCourseTitle(course != null ? course.getTitle() : "未知课程");
                courseInfo.setTeacherRealName(teacher != null ? teacher.getRealName() : "未知教师");
                courses.add(courseInfo);
            }
            dto.setCourses(courses);
            result.add(dto);
        }

        return result;
    }
}