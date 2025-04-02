package sdu.codeeducationplat.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import sdu.codeeducationplat.mapper.TeacherMapper;
import sdu.codeeducationplat.model.Teacher;
import sdu.codeeducationplat.util.BindCodeGenerator;

import java.time.LocalDateTime;

@Service
public class TeacherService extends ServiceImpl<TeacherMapper, Teacher> {

    public void createTeacher(Long uid, Long schoolId, String realName) {
        // 检查是否已存在
        if (exists(new QueryWrapper<Teacher>().eq("uid", uid))) {
            throw new RuntimeException("该用户已是教师");
        }

        Teacher teacher = new Teacher();
        teacher.setUid(uid);
        teacher.setSchoolId(schoolId);
        teacher.setRealName(realName);

        // 生成唯一绑定码
        String bindCode;
        do {
            bindCode = BindCodeGenerator.generateBindCode();
        } while (exists(new QueryWrapper<Teacher>().eq("bind_code", bindCode)));
        teacher.setBindCode(bindCode);

        save(teacher);
    }

    // 根据绑定码查询教师
    public Teacher findByBindCode(String bindCode) {
        return getOne(new QueryWrapper<Teacher>().eq("bind_code", bindCode));
    }

}