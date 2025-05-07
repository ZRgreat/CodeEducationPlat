package sdu.codeeducationplat.service.user;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.codeeducationplat.mapper.course.SchoolMapper;
import sdu.codeeducationplat.mapper.user.TeacherMapper;
import sdu.codeeducationplat.dto.identity.TeacherProfileDTO;
import sdu.codeeducationplat.model.user.Teacher;
import sdu.codeeducationplat.util.BindCodeGenerator;

@Slf4j
@Service
@RequiredArgsConstructor
public class TeacherService extends ServiceImpl<TeacherMapper, Teacher> {

    private final SchoolMapper schoolMapper;
    private final TeacherMapper teacherMapper;

    @Transactional(rollbackFor = Exception.class)
    public void createTeacher(Long uid, Long schoolId, String realName) {
        if (exists(new QueryWrapper<Teacher>().eq("uid", uid))) {
            throw new RuntimeException("该用户已是教师");
        }

        Teacher teacher = new Teacher();
        teacher.setUid(uid);
        teacher.setSchoolId(schoolId);
        teacher.setRealName(realName);

        String bindCode;
        do {
            bindCode = BindCodeGenerator.generateTeacherBindCode();
        } while (exists(new QueryWrapper<Teacher>().eq("bind_code", bindCode)));
        teacher.setBindCode(bindCode);

        save(teacher);
    }

    public Teacher findByBindCode(String bindCode) {
        return getOne(new QueryWrapper<Teacher>().eq("bind_code", bindCode));
    }

    public Long getTeacherIdByAuthentication(Authentication authentication) {
        Long uid = validateTeacherExists(authentication);
        Teacher teacher = this.getOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getUid, uid));
        return teacher.getTeacherId();
    }

    public Long validateTeacherExists(Authentication authentication) {
        Long uid = (Long) authentication.getPrincipal();
        Teacher teacher = this.getOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getUid, uid));
        if (teacher == null) {
            log.warn("教师记录不存在，uid: {}", uid);
            throw new IllegalStateException("教师记录不存在，uid: " + uid);
        }
        return uid;
    }

    public Teacher getTeacherByUid(Long uid) {
        Teacher teacher = this.getOne(new LambdaQueryWrapper<Teacher>().eq(Teacher::getUid, uid));
        if (teacher == null) {
            log.warn("教师记录不存在，uid: {}", uid);
            throw new IllegalStateException("教师记录不存在，uid: " + uid);
        }
        return teacher;
    }

    public TeacherProfileDTO getTeacherProfileByUid(Long uid) {
        Teacher teacher = getTeacherByUid(uid);
        TeacherProfileDTO dto = new TeacherProfileDTO();
        dto.setTeacherId(teacher.getTeacherId());
        dto.setUid(teacher.getUid());
        dto.setSchoolId(teacher.getSchoolId());
        dto.setRealName(teacher.getRealName());
        dto.setBindCode(teacher.getBindCode());
        dto.setCreatedAt(teacher.getCreatedAt());

        // 查询学校名称
        if (teacher.getSchoolId() != null) {
            String schoolName = schoolMapper.selectSchoolNameById(teacher.getSchoolId());
            dto.setSchoolName(schoolName != null ? schoolName : "未知学校");
        } else {
            dto.setSchoolName("无");
        }

        return dto;
    }

}