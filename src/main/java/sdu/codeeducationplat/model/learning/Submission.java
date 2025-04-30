package sdu.codeeducationplat.model.learning;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("submission")
public class Submission {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long questionId;
    private Long studentUid;
    private String submittedCode;
    private String status; // AC, WA, TLE, MLE, CE
    private String errorMessage; // "第2个测试用例失败，期望输出5，实际输出4"
    private LocalDateTime submittedAt;
    private Integer attemptCount; // 尝试次数
    private Boolean isSolved; // 是否解决
    private Long assignmentId; // 关联作业，可为空
    private Integer score; // 得分（自动或人工）
    private Boolean isGraded; // 是否已批改
}
