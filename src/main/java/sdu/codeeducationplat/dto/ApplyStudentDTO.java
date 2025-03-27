package sdu.codeeducationplat.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ApplyStudentDTO {
    @NotBlank(message = "用户ID不能为空")
    @Size(max = 6, message = "用户ID长度不能超过6")
    private Long uid;

    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @Size(max = 20, message = "学号长度不能超过20")
    private String studentNumber;

    @NotBlank(message = "学生姓名不能为空")
    @Size(max = 50, message = "学生姓名长度不能超过50")
    private String studentName;
}