package sdu.codeeducationplat.service.course;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.codeeducationplat.dto.classCourse.ClassDTO;
import sdu.codeeducationplat.dto.classCourse.CourseDTO;
import sdu.codeeducationplat.mapper.course.ClassMapper;
import sdu.codeeducationplat.mapper.course.ClassStudentMapper;
import sdu.codeeducationplat.mapper.course.CourseMapper;
import sdu.codeeducationplat.model.course.Class;
import sdu.codeeducationplat.model.course.Course;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService extends ServiceImpl<CourseMapper, Course> {

    private final ClassMapper classMapper;
    private final CourseMapper courseMapper;
    private final ClassStudentMapper classStudentMapper;

    @Transactional(rollbackFor = Exception.class)
    public CourseDTO createCourse(CourseDTO dto) {
        if (dto.getSchoolId() == null) {
            throw new IllegalStateException("学校ID不能为空，teacherUid: " + dto.getTeacherUid());
        }

        Course course = new Course();
        BeanUtils.copyProperties(dto, course);
        this.save(course);
        BeanUtils.copyProperties(course, dto);
        return dto;
    }

    @Transactional(rollbackFor = Exception.class)
    public CourseDTO updateCourse(Long id, CourseDTO dto) {
        Course course = this.getById(id);
        if (course == null) {
            throw new IllegalStateException("课程不存在，ID: " + id);
        }

        if (dto.getSchoolId() == null) {
            throw new IllegalStateException("学校ID不能为空，teacherUid: " + dto.getTeacherUid());
        }

        course.setTitle(dto.getTitle());
        course.setSchoolId(dto.getSchoolId());
        this.updateById(course);
        BeanUtils.copyProperties(course, dto);
        return dto;
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteCourse(Long id) {
        Course course = this.getById(id);
        if (course == null) {
            throw new IllegalStateException("课程不存在，ID: " + id);
        }
        classMapper.delete(new LambdaQueryWrapper<Class>().eq(Class::getCourseId, id));
        this.removeById(id);
    }

    public List<CourseDTO> listCourses(Long teacherUid) {
        List<Course> courses = this.list(new LambdaQueryWrapper<Course>()
                .eq(Course::getTeacherUid, teacherUid));
        return courses.stream().map(course -> {
            CourseDTO dto = new CourseDTO();
            BeanUtils.copyProperties(course, dto);
            return dto;
        }).collect(Collectors.toList());
    }

    public List<ClassDTO> getClassesByCourseId(Long courseId) {
        List<Class> classes = classMapper.selectList(new LambdaQueryWrapper<Class>()
                .eq(Class::getCourseId, courseId));
        return classes.stream().map(cls -> {
            ClassDTO dto = new ClassDTO();
            BeanUtils.copyProperties(cls, dto);
            long studentCount = classStudentMapper.selectCount(
                    new LambdaQueryWrapper<sdu.codeeducationplat.model.course.ClassStudent>()
                            .eq(sdu.codeeducationplat.model.course.ClassStudent::getClassId, cls.getClassId()));
            dto.setStudentCount((int) studentCount);
            return dto;
        }).collect(Collectors.toList());
    }

    public CourseDTO getDtoById(Long id) {
        Course course = this.getById(id);
        if (course == null) {
            return null;
        }
        CourseDTO dto = new CourseDTO();
        BeanUtils.copyProperties(course, dto);
        return dto;
    }

    public Course getCourseById(Long courseId) {
        if (courseId == null) {
            return null;
        }
        return courseMapper.selectById(courseId);
    }
}