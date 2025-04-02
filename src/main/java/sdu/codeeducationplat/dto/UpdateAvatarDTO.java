package sdu.codeeducationplat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateAvatarDTO {
    @NotBlank(message = "头像 URL 不能为空")
    private String avatar;
}
