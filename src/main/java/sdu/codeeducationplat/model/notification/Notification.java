package sdu.codeeducationplat.model.notification;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("notification")
public class Notification {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId; // 接收者
    private String content; // 如 "作业即将截止"
    private LocalDateTime sentAt;
    private Boolean isRead; // 是否已读
}