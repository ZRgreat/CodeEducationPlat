package sdu.codeeducationplat.dto.identity;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateAvatarDTO {
    @NotBlank(message = "头像 URL 不能为空")
    private String avatar;
}
