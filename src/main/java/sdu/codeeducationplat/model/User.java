package sdu.codeeducationplat.model;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;


@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO) // 使用自定义 ID 生成器
    @NotBlank(message = "用户ID不能为空")
    @Size(max = 6, message = "用户ID长度不能超过6")
    private Long uid; // 6位字符串UID

    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;  //用户邮箱（注册时使用）

    @NotBlank(message = "密码不能为空")
    private String password; // 用户密码

    @NotBlank(message = "昵称不能为空")
    @Size(max = 50, message = "昵称长度不能超过50")
    private String nickname; //用户昵称

    private String avatar;  //用户头像（存储图片URL)

    private Boolean isActive;  //账户是否可用，默认为1

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt; //用户创建时间

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;  //用户更新时间
}
