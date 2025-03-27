package sdu.codeeducationplat.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import sdu.codeeducationplat.model.enums.RoleEnum;
import java.time.LocalDateTime;

@Data
@TableName("user_role")
public class UserRole {
    @TableId(type = IdType.AUTO)
    private Long id;

    @TableField("uid")
    private Long uid;

    @TableField("role")
    private RoleEnum role; // 直接使用 String，因为 ER 图中是 enum('student', 'teacher')

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}