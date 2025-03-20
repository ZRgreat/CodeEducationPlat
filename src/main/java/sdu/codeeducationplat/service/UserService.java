package sdu.codeeducationplat.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import sdu.codeeducationplat.dto.RegisterRequest;
import sdu.codeeducationplat.model.User;
import sdu.codeeducationplat.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import sdu.codeeducationplat.model.enums.RoleEnum;

/**
 * 用户服务类，处理用户相关的业务逻辑
 */
@Service
public class UserService extends ServiceImpl<UserMapper, User> {

    @Autowired
    private UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * 注册新用户
     * @param request 注册请求，包含邮箱、密码和昵称
     * @throws RuntimeException 如果邮箱已注册
     */
    public void register(RegisterRequest request) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, request.getEmail());
        if (userMapper.selectOne(queryWrapper) != null) {
            throw new RuntimeException("邮箱已注册");
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setNickname(request.getNickname()); // 设置昵称
        user.setRole(RoleEnum.STUDENT);
        user.setIsActive(true);
        userMapper.insert(user);
    }

    /**
     * 用户登录
     * @param email 用户邮箱
     * @param password 用户密码
     * @return 登录成功的用户对象
     * @throws RuntimeException 如果邮箱或密码错误
     */
    public User login(String email, String password) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        User user = userMapper.selectOne(queryWrapper);
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("邮箱或密码错误");
        }
        return user;
    }

    /**
     * 更新用户昵称
     * @param uid 用户 ID
     * @param nickname 新昵称
     * @throws RuntimeException 如果用户不存在
     */
    public void updateNickname(String uid, String nickname) {
        User user = getById(uid);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setNickname(nickname);
        updateById(user);
    }

    /**
     * 更新用户头像
     * @param uid 用户 ID
     * @param avatar 新头像 URL
     * @return 更新后的头像 URL
     * @throws RuntimeException 如果用户不存在
     */
    public String updateAvatar(String uid, String avatar) {
        User user = getById(uid);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setAvatar(avatar);
        updateById(user);
        return avatar;
    }

    /**
     * 获取用户详情
     * @param uid 用户 ID
     * @return 用户对象
     * @throws RuntimeException 如果用户不存在
     */
    public User getUserDetails(String uid) {
        User user = getById(uid);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        return user;
    }
    /**
     * 重置用户密码（管理员权限）
     * @param uid 用户 ID
     * @param newPassword 新密码
     * @throws RuntimeException 如果用户不存在
     */
    public void resetPassword(String uid, String newPassword) {
        User user = getById(uid);
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        updateById(user);
    }
}