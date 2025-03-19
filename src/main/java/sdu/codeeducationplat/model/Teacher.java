package sdu.codeeducationplat.model;

import com.baomidou.mybatisplus.annotation.FieldFill;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableField;
import sdu.codeeducationplat.model.enums.CertificationStatus;

@Data
@TableName("teacher")
public class Teacher {

    @TableId
    private Long id;  //id

    @NotBlank
    @Size(max = 20)
    private String teacherNum;  //教师职工号,使用职工号进行登录

    @NotBlank
    @Size(max = 50)
    private String teacherName;  //教师姓名

    private String password;  //教师密码

    @TableField("certified")
    private CertificationStatus certified = CertificationStatus.PENDING;  //默认为待审核

    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime = LocalDateTime.now();  //创建时间


}
