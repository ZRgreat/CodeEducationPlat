package sdu.codeeducationplat.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.codeeducationplat.mapper.AdminMapper;
import sdu.codeeducationplat.model.user.Admin;

@Service
public class AdminService extends ServiceImpl<AdminMapper, Admin> {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * @param email 管理员的用户名
     * @param password 用户密码
     * @return
     */
    @Transactional
    public Admin login(String email, String password) {
        LambdaQueryWrapper<Admin> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Admin::getEmail, email); // 实际项目中应加密比对
        Admin admin = adminMapper.selectOne(queryWrapper);
        if (admin == null || !passwordEncoder.matches(password, admin.getPassword())) {
            throw new RuntimeException("邮箱或密码错误");
        }
        return admin;
    }
}