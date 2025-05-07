package sdu.codeeducationplat.service.course;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.codeeducationplat.dto.classCourse.ClassDTO;
import sdu.codeeducationplat.dto.identity.StudentDTO;
import sdu.codeeducationplat.dto.identity.TeacherDTO;
import sdu.codeeducationplat.mapper.course.ClassMapper;
import sdu.codeeducationplat.mapper.course.ClassStudentMapper;
import sdu.codeeducationplat.mapper.user.StudentMapper;
import sdu.codeeducationplat.mapper.user.UserMapper;
import sdu.codeeducationplat.model.course.Class;
import sdu.codeeducationplat.model.course.ClassStudent;
import sdu.codeeducationplat.model.course.Course;
import sdu.codeeducationplat.model.school.School;
import sdu.codeeducationplat.model.user.Student;
import sdu.codeeducationplat.model.user.Teacher;
import sdu.codeeducationplat.model.user.User;
import sdu.codeeducationplat.service.school.SchoolService;
import sdu.codeeducationplat.service.user.TeacherService;
import sdu.codeeducationplat.util.BindCodeGenerator;

import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ClassService extends ServiceImpl<ClassMapper, Class> {

    private final ClassStudentMapper classStudentMapper;
    private final CourseService courseService;
    private final StudentMapper studentMapper;
    private final UserMapper userMapper;
    private final TeacherService teacherService;
    private final SchoolService schoolService;
    private final ClassMapper classMapper;

    @Transactional(rollbackFor = Exception.class)
    public ClassDTO createClass(ClassDTO dto) {
        Course course = courseService.getById(dto.getCourseId());
        if (course == null) {
            throw new IllegalStateException("课程不存在，ID: " + dto.getCourseId());
        }
        if (!course.getTeacherUid().equals(dto.getTeacherUid())) {
            throw new IllegalStateException("无权为此课程创建班级");
        }
        Class cls = new Class();
        BeanUtils.copyProperties(dto, cls);
        cls.setSchoolId(course.getSchoolId());
        cls.setBindingCode(BindCodeGenerator.generateClassBindCode());
        this.save(cls);
        BeanUtils.copyProperties(cls, dto);
        dto.setCourseTitle(course.getTitle());
        School school = schoolService.getById(cls.getSchoolId());
        dto.setSchoolName(school != null ? school.getName() : "未知学校");
        return dto;
    }

    @Transactional(rollbackFor = Exception.class)
    public ClassDTO updateClass(Long id, ClassDTO dto) {
        Class cls = this.getById(id);
        if (cls == null) {
            throw new IllegalStateException("班级不存在，ID: " + id);
        }
        Course course = courseService.getById(dto.getCourseId());
        if (course == null) {
            throw new IllegalStateException("课程不存在，ID: " + dto.getCourseId());
        }
        if (!cls.getTeacherUid().equals(dto.getTeacherUid()) || !course.getTeacherUid().equals(dto.getTeacherUid())) {
            throw new IllegalStateException("无权更新此班级");
        }
        cls.setName(dto.getName());
        cls.setSemesterYear(dto.getSemesterYear());
        cls.setSemesterSeason(dto.getSemesterSeason());
        cls.setSchoolId(course.getSchoolId());
        this.updateById(cls);
        BeanUtils.copyProperties(cls, dto);
        dto.setCourseTitle(course.getTitle());
        School school = schoolService.getById(cls.getSchoolId());
        dto.setSchoolName(school != null ? school.getName() : "未知学校");
        return dto;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteClass(Long id) {
        Class cls = this.getById(id);
        if (cls == null) {
            throw new IllegalStateException("班级不存在，ID: " + id);
        }
        classStudentMapper.delete(new LambdaQueryWrapper<sdu.codeeducationplat.model.course.ClassStudent>()
                .eq(sdu.codeeducationplat.model.course.ClassStudent::getClassId, id));
        this.removeById(id);
    }

    @Transactional(readOnly = true)
    public ClassDTO getClassDtoById(Long id) {
        Class cls = this.getById(id);
        if (cls == null) {
            return null;
        }
        ClassDTO dto = new ClassDTO();
        BeanUtils.copyProperties(cls, dto);
        long studentCount = classStudentMapper.selectCount(
                new LambdaQueryWrapper<ClassStudent>()
                        .eq(ClassStudent::getClassId, cls.getClassId()));
        dto.setStudentCount((int) studentCount);
        Course course = courseService.getById(cls.getCourseId());
        if (course != null) {
            dto.setCourseTitle(course.getTitle());
        } else {
            log.warn("Course not found for courseId: {}", cls.getCourseId());
            dto.setCourseTitle("未知课程");
        }
        School school = schoolService.getById(cls.getSchoolId());
        if (school != null) {
            dto.setSchoolName(school.getName());
        } else {
            log.warn("School not found for schoolId: {}", cls.getSchoolId());
            dto.setSchoolName("未知学校");
        }
        return dto;
    }

    public boolean hasClassPermission(Long classId, Object principal) {
        Long teacherUid = (Long) principal; // 假设 principal 是 Long
        Class cls = this.getById(classId);
        return cls != null && cls.getTeacherUid().equals(teacherUid);
    }

    public TeacherDTO getTeacherInfo(Long classId) {
        Class cls = this.getById(classId);
        if (cls == null) {
            throw new IllegalStateException("班级不存在，ID: " + classId);
        }
        User user = userMapper.selectOne(new LambdaQueryWrapper<User>()
                .eq(User::getUid, cls.getTeacherUid()));
        if (user == null) {
            throw new IllegalStateException("教师用户不存在，uid: " + cls.getTeacherUid());
        }
        Teacher teacher = teacherService.getOne(new LambdaQueryWrapper<Teacher>()
                .eq(Teacher::getUid, cls.getTeacherUid()));
        if (teacher == null) {
            throw new IllegalStateException("教师记录不存在，uid: " + cls.getTeacherUid());
        }
        TeacherDTO dto = new TeacherDTO();
        dto.setRealName(teacher.getRealName());
        dto.setAvatar(user.getAvatar());
        return dto;
    }

    public Page<StudentDTO> getClassStudents(Long classId, int page, int pageSize) {
        Class cls = this.getById(classId);
        if (cls == null) {
            throw new IllegalStateException("班级不存在，ID: " + classId);
        }
        Page<ClassStudent> pageRequest = new Page<>(page, pageSize);
        Page<ClassStudent> classStudentsPage = classStudentMapper.selectPage(
                pageRequest,
                new LambdaQueryWrapper<ClassStudent>()
                        .eq(ClassStudent::getClassId, classId));
        Page<StudentDTO> resultPage = new Page<>(page, pageSize);
        resultPage.setTotal(classStudentsPage.getTotal());
        resultPage.setRecords(classStudentsPage.getRecords().stream()
                .map(cs -> {
                    Student student = studentMapper.selectById(cs.getStudentId());
                    if (student == null) {
                        return null;
                    }
                    StudentDTO dto = new StudentDTO();
                    dto.setStudentName(student.getStudentName());
                    dto.setStudentNumber(student.getStudentNumber());
                    return dto;
                })
                .filter(Objects::nonNull)
                .sorted(Comparator.comparing(StudentDTO::getStudentNumber))
                .collect(Collectors.toList()));
        return resultPage;
    }

    /**
     * 根据绑定码查询班级
     * @param bindingCode 绑定码
     * @return 班级信息
     */
    public Class getClassByBindingCode(String bindingCode) {
        LambdaQueryWrapper<Class> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Class::getBindingCode, bindingCode);
        return classMapper.selectOne(queryWrapper);
    }

    public Class getClassById(Long classId) {
        if (classId == null) {
            return null;
        }
        return classMapper.selectById(classId);
    }
}