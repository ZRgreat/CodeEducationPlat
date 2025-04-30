package sdu.codeeducationplat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import sdu.codeeducationplat.model.enums.Difficulty;
import sdu.codeeducationplat.model.enums.QuestionType;
import sdu.codeeducationplat.model.question.*;

import java.util.List;

@Data
public class QuestionDTO {
    @NotBlank(message = "题目标题不能为空")
    private String title;

    @NotBlank(message = "题目描述不能为空")
    private String description;

    @NotNull(message = "题目类型不能为空")
    private QuestionType type;

    @NotNull(message = "分值不能为空")
    private Integer score;

    @NotNull(message = "难度不能为空")
    private Difficulty difficulty;

    private Long authorUid;

    private String hints;

    private Boolean isPublic;

    private Long categoryId;

    private List<QuestionChoice> choices;
    private List<QuestionBlank> blanks;
    private QuestionCode code;
    private QuestionSubjective subjective;
    private List<QuestionTestCase> testCases;
    private String answer;
    private List<Long> tagIds;
}