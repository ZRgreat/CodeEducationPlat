package sdu.codeeducationplat.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sdu.codeeducationplat.dto.UserSchoolBindingDTO;
import sdu.codeeducationplat.mapper.UserSchoolBindingMapper;
import sdu.codeeducationplat.model.UserSchoolBinding;
import sdu.codeeducationplat.model.User;
import sdu.codeeducationplat.model.School;
import sdu.codeeducationplat.model.enums.RoleEnum;

import java.util.List;

@Service
public class UserSchoolBindingService extends ServiceImpl<UserSchoolBindingMapper, UserSchoolBinding> {

    @Autowired
    private UserService userService;

    @Autowired
    private SchoolService schoolService;

    public void bindSchool(UserSchoolBindingDTO dto) {
        // 验证用户和学校是否存在
        User user = userService.getById(dto.getUid());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }
        if (user.getRole() != RoleEnum.STUDENT) {
            throw new RuntimeException("只有学生可以绑定学校");
        }
        School school = schoolService.getById(dto.getSchoolId());
        if (school == null) {
            throw new RuntimeException("学校不存在");
        }

        // 检查是否已绑定该学校
        long bindingCount = count(new QueryWrapper<UserSchoolBinding>()
                .eq("uid", dto.getUid())
                .eq("school_id", dto.getSchoolId()));
        if (bindingCount > 0) {
            throw new RuntimeException("用户已绑定该学校");
        }

        // 检查学号是否在学校内已存在
        if (dto.getStudentNumber() != null && !dto.getStudentNumber().isEmpty()) {
            long studentNumberCount = count(new QueryWrapper<UserSchoolBinding>()
                    .eq("school_id", dto.getSchoolId())
                    .eq("student_number", dto.getStudentNumber()));
            if (studentNumberCount > 0) {
                throw new RuntimeException("学号在该学校内已存在");
            }
        }

        // 创建绑定
        UserSchoolBinding binding = new UserSchoolBinding();
        binding.setUid(dto.getUid());
        binding.setSchoolId(dto.getSchoolId());
        binding.setStudentNumber(dto.getStudentNumber());
        binding.setStudentName(dto.getStudentName());
        save(binding);
    }

    public List<UserSchoolBinding> getUserBindings(String uid) {
        return list(new QueryWrapper<UserSchoolBinding>().eq("uid", uid));
    }
}