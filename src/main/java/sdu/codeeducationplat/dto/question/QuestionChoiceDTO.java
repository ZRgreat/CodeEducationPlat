package sdu.codeeducationplat.dto.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuestionChoiceDTO {
    @NotBlank(message = "选项标签不能为空")
    private String label; // 选项标签：A, B, C, D

    @NotBlank(message = "选项内容不能为空")
    private String content; // 选项内容

    @NotNull(message = "是否正确不能为空")
    private Boolean isCorrect; // 是否正确
}