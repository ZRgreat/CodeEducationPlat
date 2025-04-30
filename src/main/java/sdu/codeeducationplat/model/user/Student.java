package sdu.codeeducationplat.model.user;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("student")
public class Student {
    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "用户ID不能为空")
    @Size(max = 6, message = "用户ID长度不能超过6")
    private Long uid;

    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @Size(max = 20, message = "学号长度不能超过20")
    private String studentNumber;

    @NotBlank(message = "学生姓名不能为空")
    @Size(max = 50, message = "学生姓名长度不能超过50")
    private String studentName;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(exist = false)
    private String schoolName;
}