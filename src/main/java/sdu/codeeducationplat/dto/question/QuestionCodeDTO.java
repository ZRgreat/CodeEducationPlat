package sdu.codeeducationplat.dto.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class QuestionCodeDTO {
    @NotBlank(message = "编程语言不能为空")
    private String language; // 语言：JAVA, PYTHON, CPP, C

    @NotBlank(message = "代码框架不能为空")
    private String codeStub; // 代码框架

    private String standardCode; // 标准答案代码，可选

    private String testCode; // 测试代码，可选

    @NotNull(message = "时间限制不能为空")
    @Positive(message = "时间限制必须大于0")
    private Integer timeLimit; // 时间限制（秒）

    @NotNull(message = "内存限制不能为空")
    @Positive(message = "内存限制必须大于0")
    private Integer memoryLimit; // 内存限制（MB）
}