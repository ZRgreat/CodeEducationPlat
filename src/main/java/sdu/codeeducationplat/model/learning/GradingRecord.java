package sdu.codeeducationplat.model.learning;

import com.baomidou.mybatisplus.annotation.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("grading_record")
public class GradingRecord {
    @TableId(type = IdType.AUTO)
    private Long id;

    @NotNull(message = "提交 ID 不能为空")
    private Long submissionId; // 关联 Submission

    @NotNull(message = "批改教师 UID 不能为空")
    private Long teacherUid; // 批改教师

    private Integer score; // 得分
    private String comment; // 评语
    private LocalDateTime gradedAt;
}