package sdu.codeeducationplat.model;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("class")
public class Class {

    @TableId(type = IdType.AUTO) // 自增主键
    private Long classId;

    /**
     * 教师ID，外键，关联Teacher.teacherId
     */
    @NotNull(message = "教师ID不能为空")
    private Long teacherId;

    /**
     * 班级名称
     */
    @NotBlank(message = "班级名称不能为空")
    @Size(max = 100, message = "班级名称长度不能超过100")
    private String name;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
