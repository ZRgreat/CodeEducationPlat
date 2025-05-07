package sdu.codeeducationplat.model.question;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("question_category")
public class QuestionCategory {

    @TableId
    private Long id;

    private Long questionId;

    private Long categoryId;
}