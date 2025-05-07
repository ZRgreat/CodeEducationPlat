package sdu.codeeducationplat.dto.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class QuestionBlankDTO {
    @NotNull(message = "填空序号不能为空")
    @Positive(message = "填空序号必须大于0")
    private Integer blankIndex; // 空白序号

    @NotBlank(message = "填空答案不能为空")
    private String answers; // 答案（JSON 数组字符串，如 ["answer1", "answer2"]）

    @NotNull(message = "填空分值不能为空")
    @Positive(message = "填空分值必须大于0")
    private Integer score; // 每空分值
}