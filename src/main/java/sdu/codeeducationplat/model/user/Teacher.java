package sdu.codeeducationplat.model.user;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("teacher")
public class Teacher {

    @TableId(type = IdType.AUTO)
    private Long teacherId;

    @NotBlank(message = "用户ID不能为空")
    @Size(max = 6, message = "用户ID长度不能超过36")
    private Long uid;

    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @NotBlank(message = "真实姓名不能为空")
    @Size(max = 50, message = "真实姓名长度不能超过50")
    private String realName;

    @NotBlank(message = "绑定码不能为空")
    @Size(max = 20, message = "绑定码长度不能超过20")
    private String bindCode;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
