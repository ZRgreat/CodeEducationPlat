package sdu.codeeducationplat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UpdateNicknameDTO {
    @NotBlank(message = "昵称不能为空")
    private String nickname;
}
