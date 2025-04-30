package sdu.codeeducationplat.model.question;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("question_comment")
public class QuestionComment {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long questionId;
    private Long userId;
    private String content;
    private LocalDateTime postedAt;
}