package sdu.codeeducationplat.model.question;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@TableName("category")
public class Category {
    @TableId(type = IdType.AUTO)
    private Long id;

    @NotBlank(message = "分类名称不能为空")
    private String name; // 如 "基础算法"

    private Long parentId; // 支持层级分类，可为空
}