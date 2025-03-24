package sdu.codeeducationplat.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "更新昵称请求")
public class UpdateNicknameRequest {
    @Schema(description = "新昵称", example = "NewNickname", required = true)
    private String nickname;
}