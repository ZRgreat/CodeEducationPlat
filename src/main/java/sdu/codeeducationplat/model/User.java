package sdu.codeeducationplat.model;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;
import sdu.codeeducationplat.model.enums.ApplicationStatus;
import sdu.codeeducationplat.model.enums.UserRole;

@Data
@TableName("user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;  //id

    @NotBlank
    @Email
    private String email; //用户邮箱（登录用）

    @NotBlank
    @Size(min = 8, max = 16, message = "密码长度必须在8-16位之间")
    private String rawPassword; // 用户输入的原始密码（仅用于注册时）
    private String password; // 数据库存储的加密密码

    @NotBlank
    private String nickname; //用户昵称

    private String avatar;  //用户头像（存储图片URL)

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;  //创建时间

    @TableField("role")
    private UserRole role;  // 角色（ADMIN, STUDENT, TEACHER）

    private Long schoolId;  // 绑定的学校ID（可为空）
    private String name;  // 真实姓名（校园绑定后）
    private String studentNum;  // 学号（校园绑定后）

    private ApplicationStatus applicationStatus; // // 教师申请状态（PENDING, APPROVED, REJECTED）
}
