package sdu.codeeducationplat.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TeacherProfileDTO {

    private Long teacherId;
    private Long uid;
    private Long schoolId;
    private String schoolName;
    private String realName;
    private String bindCode;
    private LocalDateTime createdAt;
}