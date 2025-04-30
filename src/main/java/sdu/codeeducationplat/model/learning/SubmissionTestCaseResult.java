package sdu.codeeducationplat.model.learning;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("submission_test_case_result")
public class SubmissionTestCaseResult {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long submissionId;
    private Long testCaseId;
    private String actualOutput; // 实际输出
    private Boolean isPassed; // 是否通过
}