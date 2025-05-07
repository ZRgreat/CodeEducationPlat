package sdu.codeeducationplat.dto.identity;

import lombok.Data;

@Data
public class UserQueryDTO {
    // 支持通过昵称或邮箱模糊搜索
    private String keyword;

    // 用户激活状态：true-启用，false-禁用，null-全部
    private Boolean isActive;

    // 分页参数（可选）
    private Integer page = 1;       // 页码，默认第一页
    private Integer pageSize = 10;  // 每页数量，默认10条
}
