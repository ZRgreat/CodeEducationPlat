package sdu.codeeducationplat.model.school;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@TableName("school")
public class School {
    @TableId(type = IdType.AUTO)
    private Long schoolId;  // 学校ID

    @NotBlank@NotBlank(message = "学校名称不能为空")
    @Size(max = 100, message = "学校名称长度不能超过100")
    private String name;  // 学校名称

    @NotBlank(message = "学校标识符不能为空")
    @Size(max = 20, message = "学校标识符长度不能超过20")
    private String code;  // 学校标识符
}

