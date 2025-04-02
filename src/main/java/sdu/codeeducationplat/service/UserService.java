package sdu.codeeducationplat.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import sdu.codeeducationplat.dto.*;
import sdu.codeeducationplat.mapper.StudentMapper;
import sdu.codeeducationplat.mapper.TeacherApplicationMapper;
import sdu.codeeducationplat.mapper.UserRoleMapper;
import sdu.codeeducationplat.model.User;
import sdu.codeeducationplat.mapper.UserMapper;
import org.springframework.stereotype.Service;
import sdu.codeeducationplat.model.UserRole;
import sdu.codeeducationplat.model.enums.RoleEnum;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务类，处理用户相关的业务逻辑
 */
@Service
@RequiredArgsConstructor
public class UserService extends ServiceImpl<UserMapper, User> {

    private final UserMapper userMapper;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;

    /**
     * 注册新用户
     * @param dto 注册请求，包含邮箱、密码和昵称
     * @throws RuntimeException 如果邮箱已注册
     */
    @Transactional(rollbackFor = Exception.class)
    public User register(RegisterRequestDTO dto) {
        if (getOne(new QueryWrapper<User>().eq("email", dto.getEmail())) != null) {
            throw new RuntimeException("邮箱已被注册");
        }
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setNickname(dto.getNickname());
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        save(user);

        UserRole userRole = new UserRole();
        userRole.setUid(user.getUid());
        userRole.setRole(RoleEnum.USER);
        userRole.setCreatedAt(LocalDateTime.now());
        userRoleService.save(userRole);

        return user;
    }

    /**
     * 用户登录
     * @param email 用户邮箱
     * @param password 用户密码
     * @return UserWithRoleDTO
     * @throws RuntimeException 如果邮箱或密码错误
     */
    @Transactional(rollbackFor = Exception.class)
    public UserWithRoleDTO login(String email, String password) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("邮箱或密码错误");
        }
        if (!user.getIsActive()) {
            throw new RuntimeException("账户已被禁用");
        }
        //在userRole中读取
        List<UserRole> userRoles = userRoleService.list(new QueryWrapper<UserRole>().eq("uid", user.getUid()));        UserWithRoleDTO dto = new UserWithRoleDTO();
        dto.setUid(user.getUid());
        dto.setEmail(user.getEmail());
        dto.setNickname(user.getNickname());
        // 提取角色列表
        List<RoleEnum> roles = userRoles.stream()
                .map(UserRole::getRole) // 假设 UserRole 有一个 getRole 方法返回 RoleEnum
                .collect(Collectors.toList());
        dto.setRoles(roles);
        return dto;
    }

    /**
     * 获取用户详情
     * @param uid 用户 ID
     * @return 用户对象
     * @throws RuntimeException 如果用户不存在
     */
    public UserProfileDTO getUserDetails(Long uid) {
        User user = getById(uid);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        List<UserRole> userRoles = userRoleService.list(new QueryWrapper<UserRole>().eq("uid", uid));
        List<String> roles = userRoles.stream()
                .map(role -> role.getRole().name())  // 转为字符串
                .collect(Collectors.toList());

        UserProfileDTO dto = new UserProfileDTO();
        dto.setUid(user.getUid());
        dto.setNickname(user.getNickname());
        dto.setAvatar(user.getAvatar());
        dto.setEmail(user.getEmail());
        dto.setRoles(roles);
        return dto;
    }

    /**
     * 更新用户昵称
     * @param uid 用户 ID
     * @param nickname 新昵称
     * @throws RuntimeException 如果用户不存在
     */
    @Transactional
    public void updateNickname(Long uid, String nickname) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("uid", uid).set("nickname", nickname);
        update(updateWrapper);
    }

    /**
     * 更新用户头像
     * @param uid 用户 ID
     * @param avatarUrl 新头像 URL
     * @return 更新后的头像 URL
     * @throws RuntimeException 如果用户不存在
     */
    @Transactional
    public String updateAvatar(Long uid, String avatarUrl) {
        UpdateWrapper<User> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("uid", uid).set("avatar", avatarUrl);
        update(updateWrapper);
        return updateWrapper.getSqlSet();
    }

    /**
     * 用户注销（逻辑删除）
     * @param uid
     */
    public void deleteUser(Long uid) {
        this.removeById(uid);
    }


    //以下均为管理员所需功能
    /**
     * @param keyword
     * @return
     */
    public List<User> queryUsersByKeyword(String keyword) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(User::getNickname, keyword).or().like(User::getEmail, keyword);
        }
        return list(wrapper);
    }
    /**
     * 解禁/封禁用户（管理员权限）
     * @param uid
     * @param active
     */
    @Transactional(rollbackFor = Exception.class)
    public void setUserActiveStatus(Long uid, boolean active) {
        User user = getById(uid);
        if (user != null) {
            user.setIsActive(active);
            updateById(user);
        }
    }

    /**
     * 重置用户密码（管理员权限）
     * @param uid 用户 ID
     */
    public void resetPassword(Long uid) {
        User user = getById(uid);
        if (user != null) {
            user.setPassword(passwordEncoder.encode("123456"));
            updateById(user);
        }
    }

}