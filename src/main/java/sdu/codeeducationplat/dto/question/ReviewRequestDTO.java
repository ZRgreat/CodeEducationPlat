package sdu.codeeducationplat.dto.question;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import sdu.codeeducationplat.model.enums.CertificationStatus;

@Data
public class ReviewRequestDTO {
    @NotNull(message = "审核状态不能为空")
    private CertificationStatus status;

    private String reviewComment;
}