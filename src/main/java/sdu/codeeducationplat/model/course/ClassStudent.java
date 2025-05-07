package sdu.codeeducationplat.model.course;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("class_student")
public class ClassStudent {
    @TableId(type = IdType.AUTO)
    private Long csId;

    @NotNull(message = "班级ID不能为空")
    private Long classId;

    @NotNull(message = "学生ID不能为空")
    private Long studentId; //用户uid

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}