package sdu.codeeducationplat.model;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import sdu.codeeducationplat.model.enums.CertificationStatus;

import java.time.LocalDateTime;

@Data
@TableName("teacher_application")
public class TeacherApplication {

    /**
     * 主键
     */
    @TableId(type = IdType.AUTO) // 自增主键
    private Long applicationId;

    /**
     * 用户ID，外键，关联User.uid
     */
    @NotNull(message = "用户ID不能为空")
    @Size(max = 6, message = "用户ID长度不能超过36")
    private Long uid;

    /**
     * 学校ID，外键，关联School.schoolId
     */
    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    /**
     * 真实姓名
     */
    @NotBlank(message = "真实姓名不能为空")
    @Size(max = 50, message = "真实姓名长度不能超过50")
    private String realName;

    /**
     * 证明材料图URL，可为空
     */
    @Size(max = 255, message = "证明材料URL长度不能超过255")
    private String proofImage;

    /**
     * 申请状态，可选值：pending, approved, rejected，默认pending
     */
    private CertificationStatus status;

    /**
     * 创建时间
     */
    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    // schoolName 字段，不存储到数据库
    @TableField(exist = false)
    private String schoolName;
}
