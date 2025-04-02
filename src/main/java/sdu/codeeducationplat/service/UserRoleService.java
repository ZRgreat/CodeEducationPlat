package sdu.codeeducationplat.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.codeeducationplat.mapper.UserRoleMapper;
import sdu.codeeducationplat.model.UserRole;
import sdu.codeeducationplat.model.enums.RoleEnum;
import java.time.LocalDateTime;
import java.util.List;


@Service
public class UserRoleService extends ServiceImpl<UserRoleMapper, UserRole> {

    /**
     * 保存或更新用户角色（用于 user ➜ student/teacher 升级）
     * - 如果原本是 user，则删除
     * - 避免重复插入 student/teacher
     *
     * @param uid 用户 UID
     * @param newRole 新角色（student 或 teacher）
     */
    @Transactional
    public void saveOrUpdateUserRole(Long uid, RoleEnum newRole) {
        // 1. 先判断当前用户是否已经拥有该角色
        if (hasRole(uid, newRole)) return;

        // 2. 查询当前用户的所有角色
        List<UserRole> roles = lambdaQuery().eq(UserRole::getUid, uid).list();

        // 3. 若当前仅有 user 角色则删除
        if (roles.size() == 1 && roles.get(0).getRole() == RoleEnum.USER) {
            removeById(roles.get(0).getId());
        }

        // 4. 添加新角色
        UserRole role = new UserRole();
        role.setUid(uid);
        role.setRole(newRole);
        role.setCreatedAt(LocalDateTime.now());
        save(role);
    }

    /**
     * 判断用户是否拥有指定角色
     * @param uid 用户id
     * @param role 角色枚举
     * @return 是否拥有角色
     */
    public boolean hasRole(Long uid, RoleEnum role) {
        return count(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUid, uid)
                .eq(UserRole::getRole, role)) > 0;
    }



}
