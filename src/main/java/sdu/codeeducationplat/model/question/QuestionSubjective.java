package sdu.codeeducationplat.model.question;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@TableName("question_subjective")
public class QuestionSubjective {

    @TableId(type = IdType.AUTO)
    private Long id;

    @NotNull(message = "题目 ID 不能为空")
    private Long questionId;

    private Boolean requireUpload;

    private String gradingCriteria;

    private String rubric; // {"内容完整性": 5, "逻辑清晰": 3}
}