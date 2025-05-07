package sdu.codeeducationplat.dto.classCourse;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class JoinClassDTO {

    @NotBlank(message = "绑定码不能为空")
    @Size(max = 20, message = "绑定码长度不能超过20")
    private String bindingCode;
}