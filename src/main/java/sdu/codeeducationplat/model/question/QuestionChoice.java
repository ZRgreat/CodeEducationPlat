package sdu.codeeducationplat.model.question;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@TableName("question_choice")
public class QuestionChoice {

    @TableId(type = IdType.AUTO)
    private Long id;

    @NotNull(message = "题目 ID 不能为空")
    private Long questionId;

    @NotBlank(message = "选项标签不能为空")
    private String label;

    @NotBlank(message = "选项内容不能为空")
    private String content;

    private Boolean isCorrect;
}