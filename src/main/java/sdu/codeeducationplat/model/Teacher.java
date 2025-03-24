package sdu.codeeducationplat.model;

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
    @Size(max = 36, message = "用户ID长度不能超过36")
    private String uid;

    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @NotBlank(message = "真实姓名不能为空")
    @Size(max = 50, message = "真实姓名长度不能超过50")
    private String realName;

    @Size(max = 50, message = "绑定码长度不能超过50")
    private String bindingCode;

    @NotNull(message = "创建时间不能为空")
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
