package sdu.codeeducationplat.dto.classCourse;

import lombok.Data;

import java.util.List;

@Data
public class StudentCourseDTO {
    // 学生身份信息
    private Long studentId; // 对应 student.uid
    private String schoolName;
    private String studentNumber;
    private String studentName;
    // 课程和班级信息
    private List<CourseInfo> courses;

    @Data
    public static class CourseInfo {
        private Long classId;
        private String courseTitle;
        private String className;
        private String teacherRealName;
    }
}