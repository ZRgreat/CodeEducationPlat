package sdu.codeeducationplat.model.course;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("course")
public class Course {
    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "课程名称不能为空")
    private String title;

    @NotNull(message = "教师ID不能为空")
    private Long teacherUid;

    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}