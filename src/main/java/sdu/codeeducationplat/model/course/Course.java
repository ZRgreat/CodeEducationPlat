package sdu.codeeducationplat.model.course;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("course")
public class Course {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private Long teacherUid;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
