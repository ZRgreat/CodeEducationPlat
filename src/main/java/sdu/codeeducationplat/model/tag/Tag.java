package sdu.codeeducationplat.model.tag;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@TableName("tag")
public class Tag {
    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "标签名称不能为空")
    private String name; // 如 "数组", "动态规划"
}