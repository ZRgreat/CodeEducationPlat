package sdu.codeeducationplat.model.learning;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("wrong_answer_note")
public class WrongAnswerNote {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long studentUid;
    private Long questionId;
    private String note; // 学生反思或教师建议
}