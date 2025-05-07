package sdu.codeeducationplat.dto.question;

import lombok.Data;
import sdu.codeeducationplat.model.enums.CertificationStatus;
import sdu.codeeducationplat.model.enums.Difficulty;
import sdu.codeeducationplat.model.enums.QuestionType;

@Data
public class QuestionQueryDTO {
    private Integer pageNum = 1;
    private Integer pageSize = 5;
    private QuestionType type;
    private Long categoryId;
    private Long authorUid;
    private CertificationStatus status;
    private String title;
    private Difficulty difficulty;
    private Boolean isPublic; //用于题库查询
}