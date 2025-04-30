package sdu.codeeducationplat.model.user;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("admin")
public class Admin {
    @TableId(type = IdType.AUTO)
    private Long adminId;

    private String username;

    private String password;

    @Email
    private String email;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}