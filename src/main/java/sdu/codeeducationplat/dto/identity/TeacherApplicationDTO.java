package sdu.codeeducationplat.dto.identity;

import lombok.Data;
import sdu.codeeducationplat.model.enums.CertificationStatus;
import java.time.LocalDateTime;

@Data
public class TeacherApplicationDTO {
    private Long applicationId;
    private Long uid;
    private Long schoolId;
    private String schoolName;
    private String realName;
    private String proofImage;
    private CertificationStatus status;
    private LocalDateTime createdAt;
}