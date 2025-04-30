package sdu.codeeducationplat.model.course;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

@Data
@TableName("assignment")
public class Assignment {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long courseId;
    private Long questionId;
    private LocalDateTime dueDate;
    private Integer totalScore;
}