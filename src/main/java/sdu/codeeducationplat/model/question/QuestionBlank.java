package sdu.codeeducationplat.model.question;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@TableName("question_blank")
public class QuestionBlank {

    @TableId(type = IdType.AUTO)
    private Long id;

    @NotNull(message = "题目 ID 不能为空")
    private Long questionId;

    @NotNull(message = "空编号不能为空")
    private Integer blankIndex;

    @NotBlank(message = "答案不能为空")
    private String answers; // "[\"for\", \"while\"]" 或 "正则: ^\\d+$"

    @NotNull(message = "此空得分不能为空")
    private Integer score;
}