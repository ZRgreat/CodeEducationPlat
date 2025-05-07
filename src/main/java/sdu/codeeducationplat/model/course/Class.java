package sdu.codeeducationplat.model.course;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("class")
public class Class {
    @TableId(type = IdType.AUTO)
    private Long classId;

    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @NotNull(message = "教师用户ID不能为空")
    private Long teacherUid;

    @NotNull(message = "课程ID不能为空")
    private Long courseId;

    @NotBlank(message = "班级名称不能为空")
    @Size(max = 100, message = "班级名称长度不能超过100")
    private String name;

    @NotNull(message = "学年不能为空")
    private Integer semesterYear;

    @NotBlank(message = "学期不能为空")
    private String semesterSeason;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @NotBlank(message = "绑定码不能为空")
    @Size(max = 20, message = "绑定码长度不能超过20")
    private String bindingCode;
}