package sdu.codeeducationplat.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class StudentBindingDTO {
    private Long id; // 对应 s.id
    private Long uid; // 对应 s.uid
    private Long schoolId; // 对应 s.school_id AS schoolId
    private String studentNumber; // 对应 s.student_number AS studentNumber
    private String studentName; // 对应 s.student_name AS studentName
    private LocalDateTime createdAt; // 对应 s.created_at AS createdAt
    private String schoolName; // 对应 sc.name AS schoolName
}