package sdu.codeeducationplat.dto.identity;

import lombok.Data;
import sdu.codeeducationplat.model.enums.RoleEnum;

@Data
public class LoginResponseDTO {
    private Long uid;
    private String nickname;
    private String email;
    private RoleEnum role;
}

