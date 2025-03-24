package sdu.codeeducationplat.model;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_school_binding")
public class UserSchoolBinding {

    @TableId(type = IdType.AUTO) // 自增主键
    private Long bindingId;

    /**
     * 用户ID，外键，关联User.uid
     */
    @NotBlank(message = "用户ID不能为空")
    @Size(max = 36, message = "用户ID长度不能超过36")
    private String uid;

    /**
     * 学校ID，外键，关联School.schoolId
     */
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @Size(max = 20, message = "学号长度不能超过20")
    private String studentNumber;

    @Size(max = 50, message = "学生姓名长度不能超过50")
    private String studentName;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}