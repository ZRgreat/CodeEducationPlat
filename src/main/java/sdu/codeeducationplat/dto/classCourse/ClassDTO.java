package sdu.codeeducationplat.dto.classCourse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ClassDTO {
    private Long classId;

    private Long schoolId;

    @NotNull(message = "教师用户ID不能为空")
    private Long teacherUid;

    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    @NotBlank(message = "班级名称不能为空")
    private String name;

    @NotNull(message = "学年不能为空")
    private Integer semesterYear;

    @NotBlank(message = "学期不能为空")
    private String semesterSeason;

    private LocalDateTime createdAt;
    private String bindingCode;
    private Integer studentCount;

    private String courseTitle;
    private String schoolName;
}