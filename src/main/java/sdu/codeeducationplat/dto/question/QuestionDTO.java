package sdu.codeeducationplat.dto.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import sdu.codeeducationplat.model.enums.Difficulty;
import sdu.codeeducationplat.model.enums.QuestionType;
import sdu.codeeducationplat.validation.MultiChoiceConstraint;
import sdu.codeeducationplat.validation.SingleChoiceConstraint;

import java.util.List;

@SingleChoiceConstraint
@MultiChoiceConstraint
@Data
public class QuestionDTO {
    private String title; // 标题非必填

    @NotBlank(message = "题目内容不能为空")
    private String content; // DSL 或文本内容

    @NotNull(message = "题目类型不能为空")
    private QuestionType type;

    @NotNull(message = "分值不能为空")
    @Positive(message = "分值必须大于0")
    private Integer score;

    @NotNull(message = "难度不能为空")
    private Difficulty difficulty;

    private Long authorUid; // 作者 UID

    private String hints; // 提示

    private Boolean isPublic = false; // 是否公开，默认私有

    private List<Long> categoryIds; // 知识点 ID 列表，可为空

    private List<QuestionChoiceDTO> choices; // 选择题
    private List<QuestionBlankDTO> blanks; // 填空题
    private List<QuestionCodeDTO> codes; // 代码题
    private QuestionSubjectiveDTO subjective; // 主观题
    private List<QuestionTestCaseDTO> testCases; // 测试用例
    private String answer; // 判断题答案，支持 "true" 或 "false"
}