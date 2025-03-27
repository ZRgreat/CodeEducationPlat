package sdu.codeeducationplat.dto;

import lombok.Data;
import sdu.codeeducationplat.model.User;
import sdu.codeeducationplat.model.enums.RoleEnum;
import java.util.List;

/**
 * 用户基本信息以及身份列表
 */
@Data
public class UserWithRoleDTO {
    private Long uid;
    private String email;
    private String nickname;
    private List<RoleEnum> roles;
    // 构造函数或静态方法，用于从 User 和 roles 构造 DTO
    public static UserWithRoleDTO from (User user, List<RoleEnum> roles) {
        UserWithRoleDTO dto = new UserWithRoleDTO();
        dto.setUid(user.getUid());
        dto.setEmail(user.getEmail());
        dto.setNickname(user.getNickname());
        dto.setRoles(roles);
        return dto;
    }
}
