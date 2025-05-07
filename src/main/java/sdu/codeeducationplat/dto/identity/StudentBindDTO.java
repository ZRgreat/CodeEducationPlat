package sdu.codeeducationplat.dto.identity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class StudentBindDTO {

    @NotBlank(message = "绑定码不能为空")
    @Size(max = 20, message = "绑定码长度不能超过20")
    private String bindCode;

    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @NotBlank(message = "学号不能为空")
    @Size(max = 20, message = "学号长度不能超过20")
    private String studentNumber;

    @NotBlank(message = "学生姓名不能为空")
    @Size(max = 50, message = "学生姓名长度不能超过50")
    private String studentName;
}