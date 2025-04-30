package sdu.codeeducationplat.model.question;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@TableName("question_test_case")
public class QuestionTestCase {

    @TableId(type = IdType.AUTO)
    private Long id;

    @NotNull(message = "题目 ID 不能为空")
    private Long questionId;

    @NotBlank(message = "输入数据不能为空")
    private String input;

    @NotBlank(message = "预期输出不能为空")
    private String output;

    private String explanation; // "输入 [1,2,3] 表示三个数字，输出 6 是它们的和"

    private Boolean isSample;

    private Boolean isHidden; // 是否隐藏，默认 false
}