package sdu.codeeducationplat.model.question;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import sdu.codeeducationplat.model.enums.CertificationStatus;
import sdu.codeeducationplat.model.enums.Difficulty;
import sdu.codeeducationplat.model.enums.QuestionType;

import java.time.LocalDateTime;

@Data
@TableName("question")
public class Question {

    @TableId(type = IdType.AUTO)
    private Long questionId;

    private String title; // 标题非必填

    @NotBlank(message = "题目描述不能为空")
    private String description;

    @NotNull(message = "题目类型不能为空")
    private QuestionType type;

    @NotNull(message = "分值不能为空")
    private Integer score;

    @NotNull(message = "难度不能为空")
    private Difficulty difficulty;

    @NotNull(message = "创建者 UID 不能为空")
    private Long authorUid;

    private String hints;

    @TableField(value = "created_at", fill = FieldFill.INSERT)
    private LocalDateTime createdAt;

    @TableField(value = "is_public")
    private Boolean isPublic = false; // 默认私有

    @TableField(value = "is_deleted")
    private Boolean isDeleted = false;

    private Integer solvedCount = 0;

    private Integer attemptCount = 0;

    private String answer; // 存储判断题答案

    @TableField(value = "status")
    private CertificationStatus status = CertificationStatus.PENDING; // 默认待审核

    private String reviewComment;
}