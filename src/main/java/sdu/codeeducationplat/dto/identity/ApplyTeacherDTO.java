package sdu.codeeducationplat.dto.identity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ApplyTeacherDTO {

    @NotNull(message = "学校ID不能为空")
    private Long schoolId;

    @NotBlank(message = "真实姓名不能为空")
    @Size(max = 50, message = "真实姓名长度不能超过50")
    private String realName;

    @Size(max = 255, message = "证明材料URL长度不能超过255")
    private String proofImage;
}