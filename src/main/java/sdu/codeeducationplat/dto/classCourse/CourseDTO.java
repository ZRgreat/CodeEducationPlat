package sdu.codeeducationplat.dto.classCourse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourseDTO {
    private Long id;

    @NotBlank(message = "课程名称不能为空")
    private String title;

    @NotNull(message = "教师ID不能为空")
    private Long teacherUid;

    private Long schoolId;

    private LocalDateTime createdAt;
}