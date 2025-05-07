package sdu.codeeducationplat.dto.question;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class QuestionSubjectiveDTO {
    @NotNull(message = "是否需要上传附件不能为空")
    private Boolean requireUpload; // 是否需要上传附件

    private String gradingCriteria; // 评分标准，可选
}