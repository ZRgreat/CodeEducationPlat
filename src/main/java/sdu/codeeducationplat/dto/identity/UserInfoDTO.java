package sdu.codeeducationplat.dto.identity;

import lombok.Data;
import sdu.codeeducationplat.model.enums.RoleEnum;

import java.util.List;

@Data
public class UserInfoDTO {
    private Long uid;
    private String nickname;
    private String avatar;
    private String email;
    private List<RoleEnum> roles;
}
