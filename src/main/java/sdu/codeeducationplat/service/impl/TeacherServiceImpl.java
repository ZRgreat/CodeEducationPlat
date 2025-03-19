package sdu.codeeducationplat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sdu.codeeducationplat.mapper.TeacherMapper;
import sdu.codeeducationplat.model.Teacher;
import sdu.codeeducationplat.model.enums.CertificationStatus;
import sdu.codeeducationplat.service.TeacherService;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class TeacherServiceImpl implements TeacherService {
    private static final Logger logger = LoggerFactory.getLogger(TeacherServiceImpl.class);

    @Autowired
    private TeacherMapper teacherMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void addTeacher(Teacher teacher) {
        if (teacher == null) {
            throw new IllegalArgumentException("教师信息不能为空");
        }
        if (teacher.getTeacherName() == null || teacher.getTeacherName().trim().isEmpty()) {
            throw new IllegalArgumentException("姓名不能为空");
        }
        if (teacher.getTeacherNum() == null || teacher.getTeacherNum().trim().isEmpty()) {
            throw new IllegalArgumentException("职工号不能为空");
        }
        if (teacher.getPassword() == null || teacher.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("请输入密码");
        }

        // 确保职工号不重复
        QueryWrapper<Teacher> wrapper = new QueryWrapper<>();
        wrapper.eq("teacher_num", teacher.getTeacherNum());
        if (teacherMapper.selectCount(wrapper) > 0) {
            throw new RuntimeException("职工号已存在");
        }

        // 强制设置 certified 为 PENDING，防止前端篡改
        teacher.setCertified(CertificationStatus.PENDING);

        // 设置创建时间
        teacher.setCreateTime(LocalDateTime.now());

        // 加密密码
        teacher.setPassword(passwordEncoder.encode(teacher.getPassword()));

        // 插入数据库
        teacherMapper.insert(teacher);
    }

    @Override
    public void deleteTeacherById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID 不能为空");
        }
        int result = teacherMapper.deleteById(id);
        if (result == 0) {
            throw new RuntimeException("教师不存在或已被删除");
        }
    }

    @Override
    public Teacher getTeacherById(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID 无效");
        }
        Teacher teacher = teacherMapper.selectById(id);
        if (teacher == null) {
            throw new RuntimeException("教师不存在");
        }
        return teacher;
    }

    //查询
    @Override
    public List<Teacher> getTeachersByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("姓名不能为空");
        }
        List<Teacher> teachers = teacherMapper.selectTeacherListByTeacherName(name);
        if (teachers.isEmpty()) {
            throw new RuntimeException("没有找到姓名包含 '" + name + "' 的教师");
        }
        return teachers;
    }

    @Override
    public List<Teacher> getTeachersByNumName(String numName) {
        if(numName == null || numName.trim().isEmpty()){
            throw new IllegalArgumentException("职工号或者姓名不能为空");
        }
        List<Teacher> teachers = teacherMapper.selectTeacherListByNumName(numName);
        if (teachers.isEmpty()) {
            throw new RuntimeException("无");
        }
        return teachers;
    }


    @Override
    public List<Teacher> getTeachersByCertified(CertificationStatus certified) {
        if (certified == null) {
            throw new IllegalArgumentException("认证状态不能为空");
        }
        return teacherMapper.selectTeacherListByStatus(certified);
    }

/*
    @Override
    public void updateTeacher(Teacher teacher) {

    }

    @Override
    public void updateTeacherShelf(Long teacherId, Teacher teacher) {

    }
*/


}
