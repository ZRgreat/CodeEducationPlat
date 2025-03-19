package sdu.codeeducationplat.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@TableName("school")
public class School {
    @TableId(type = IdType.AUTO)
    private Long id;  // 学校ID

    @NotBlank
    private String name;  // 学校名称

    private String bindCode;  // 绑定码（教师创建班级后可生成）
}

