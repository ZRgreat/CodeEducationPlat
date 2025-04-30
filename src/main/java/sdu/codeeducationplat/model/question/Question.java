package sdu.codeeducationplat.model.question;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import sdu.codeeducationplat.model.enums.Difficulty;
import sdu.codeeducationplat.model.enums.QuestionType;

import java.time.LocalDateTime;

@Data
@TableName("question")
public class Question {

    @TableId(type = IdType.AUTO)
    private Long questionId;

    @NotBlank(message = "题目标题不能为空")
    private String title;

    @NotBlank(message = "题目描述不能为空")
    private String description; // 支持 Markdown，如 "## 问题背景\n输入一个数组..."

    @NotNull(message = "题目类型不能为空")
    private QuestionType type; // 使用枚举类

    @NotNull(message = "分值不能为空")
    private Integer score;

    @NotNull(message = "难度不能为空")
    private Difficulty difficulty; // 使用枚举类

    @NotNull(message = "创建者 UID 不能为空")
    private Long authorUid;

    private String hints; // "提示1: 考虑使用循环\n提示2: 检查边界条件"

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    private Boolean isPublic;

    private Boolean isDeleted;

    private Integer solvedCount; // 解决人数

    private Integer attemptCount; // 尝试人数

    private String relatedResources; // {"视频": "http://...", "文章": "http://..."}

    private Long categoryId; // 新增字段，分类 ID

}