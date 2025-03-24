package sdu.codeeducationplat.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import sdu.codeeducationplat.mapper.AdminMapper;
import sdu.codeeducationplat.model.Admin;

@Service
public class AdminService extends ServiceImpl<AdminMapper, Admin> {

    public Admin login(String username, String password) {
        QueryWrapper<Admin> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username)
                .eq("password", password); // 实际项目中应加密比对
        Admin admin = getOne(queryWrapper);
        if (admin == null) {
            throw new RuntimeException("用户名或密码错误");
        }
        return admin;
    }
}