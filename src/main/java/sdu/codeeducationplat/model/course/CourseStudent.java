package sdu.codeeducationplat.model.course;

import lombok.Data;
import com.baomidou.mybatisplus.annotation.*;
import java.time.LocalDateTime;

@Data
@TableName("course_student")
public class CourseStudent {
    private Long courseId;
    private Long studentUid;
    private LocalDateTime enrolledAt;
}