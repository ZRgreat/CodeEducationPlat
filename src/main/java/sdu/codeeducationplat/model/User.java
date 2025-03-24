package sdu.codeeducationplat.model;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;
import sdu.codeeducationplat.model.enums.RoleEnum;


@Data
@TableName("user")
public class User {
    @TableId(type = IdType.ASSIGN_UUID)
    private String uid; //用户编号，采用UUID格式

    private String email;  //用户邮箱（注册时使用）

    private String password; // 用户密码

    private String nickname; //用户昵称

    private String avatar;  //用户头像（存储图片URL)

    private RoleEnum role; //用户身份，默认为user

    private Boolean isActive;  //账户是否可用，默认为1

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt; //用户创建时间

    @TableField(value = "updated_at", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;  //用户更新时间
}
