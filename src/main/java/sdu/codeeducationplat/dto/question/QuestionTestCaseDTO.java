package sdu.codeeducationplat.dto.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuestionTestCaseDTO {
    @NotBlank(message = "测试用例输入不能为空")
    private String input; // 输入

    @NotBlank(message = "测试用例输出不能为空")
    private String output; // 输出

    @NotNull(message = "是否为样例不能为空")
    private Boolean isSample; // 是否为样例

    @NotNull(message = "是否隐藏不能为空")
    private Boolean isHidden; // 是否隐藏
}