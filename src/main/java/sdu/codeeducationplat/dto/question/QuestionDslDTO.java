package sdu.codeeducationplat.dto.question;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import sdu.codeeducationplat.model.enums.Difficulty;

import java.util.List;

@Data
public class QuestionDslDTO {
    private String title; // 标题非必填

    @NotBlank(message = "DSL 内容不能为空")
    private String content; // DSL 内容

    @NotNull(message = "难度不能为空")
    private Difficulty difficulty;

    private List<Long> categoryIds; // 知识点 ID 列表，可为空

    private String hints; // 提示

    private Boolean isPublic = false; // 是否公开，默认私有
}