package sdu.codeeducationplat.model.learning;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("student_progress")
public class StudentProgress {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long studentUid;
    private Long courseId;
    private Integer completedAssignments; // 已完成作业数
    private Integer totalScore; // 总得分
    private LocalDateTime lastUpdated;
}