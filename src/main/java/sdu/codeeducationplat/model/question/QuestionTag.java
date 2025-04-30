package sdu.codeeducationplat.model.question;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

@Data
@TableName("question_tag")
public class QuestionTag {
    private Long questionId;
    private Long tagId;
}