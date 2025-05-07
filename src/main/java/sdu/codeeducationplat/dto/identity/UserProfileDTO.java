package sdu.codeeducationplat.dto.identity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.List;

@Data
public class UserProfileDTO {

    @Schema(description = "用户编号:UID000000")
    private String uid;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "头像")
    private String avatar;

    @Schema(description = "邮箱")
    private String email;

    @Schema(description = "角色列表（如 STUDENT、TEACHER）")
    private List<String> roles;

    public void setUid(Long rawUid) {
        this.uid = String.format("UID%06d", rawUid);
    }
}
