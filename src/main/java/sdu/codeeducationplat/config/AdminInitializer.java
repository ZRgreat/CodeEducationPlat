package sdu.codeeducationplat.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import sdu.codeeducationplat.model.Admin;
import sdu.codeeducationplat.service.AdminService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.time.LocalDateTime;

@Slf4j
@Component
public class AdminInitializer {

    @Autowired
    private AdminService adminService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostConstruct
    public void initAdmin() {
        String defaultEmail = "admin@example.com";
        String defaultUsername = "admin";

        // 判断是否已存在管理员
        Admin existingAdmin = adminService.getOne(new QueryWrapper<Admin>()
                .eq("email", defaultEmail)
                .or()
                .eq("username", defaultUsername));

        if (existingAdmin == null) {
            Admin admin = new Admin();
            admin.setUsername(defaultUsername);
            admin.setEmail(defaultEmail);
            admin.setPassword(passwordEncoder.encode("123456")); // 加密密码
            admin.setCreatedAt(LocalDateTime.now());
            adminService.save(admin);
            log.info("已成功初始化管理员账号：[{} / {}]", defaultUsername, defaultEmail);
        } else {
            log.info("管理员账号已存在：[{} / {}]，跳过初始化", existingAdmin.getUsername(), existingAdmin.getEmail());
        }
    }
}
