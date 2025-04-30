package sdu.codeeducationplat.model.course;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("class_student")
public class ClassStudent {

    @TableId(type = IdType.AUTO) // 自增主键
    private Long csId;

    /**
     * 班级ID，外键，关联Class.classId
     */
    @NotNull(message = "班级ID不能为空")
    private Long classId;

    /**
     * 学生用户ID，外键，关联User.uid
     */
    @NotNull(message = "学生ID不能为空")
    private Long studentId;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
