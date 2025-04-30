package sdu.codeeducationplat.model.question;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@TableName("question_code")
public class QuestionCode {

    @TableId(type = IdType.AUTO)
    private Long id;

    @NotNull(message = "题目 ID 不能为空")
    private Long questionId;

    private String codeStub; // {"JAVA": "public int solution() {}", "PYTHON": "def solution():"}

    private String standardCode;

    private String testCode;

    @NotBlank(message = "编程语言不能为空")
    private String language; // CPP, C, PYTHON, JAVA

    @NotNull(message = "时间限制不能为空")
    private Integer timeLimit;

    @NotNull(message = "内存限制不能为空")
    private Integer memoryLimit;
}