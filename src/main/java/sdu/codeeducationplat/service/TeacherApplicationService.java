//package sdu.codeeducationplat.service;
//
//import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
//import com.baomidou.mybatisplus.extension.service.IService;
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import sdu.codeeducationplat.dto.TeacherApplicationDTO;
//import sdu.codeeducationplat.mapper.TeacherApplicationMapper;
//import sdu.codeeducationplat.model.*;
//import sdu.codeeducationplat.model.enums.CertificationStatus;
//import sdu.codeeducationplat.model.enums.RoleEnum;
//
//@Service
//public class TeacherApplicationService extends ServiceImpl<TeacherApplicationMapper, TeacherApplication> {
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private UserSchoolBindingService userSchoolBindingService;
//
//    @Autowired
//    private SchoolService schoolService;
//
//    @Autowired
//    private TeacherService teacherService;
//
//    public void submitApplication(TeacherApplicationDTO dto) {
//        // 验证用户是否存在
//        User user = userService.getById(dto.getUid());
//        if (user == null) {
//            throw new RuntimeException("用户不存在");
//        }
//
//        // 验证学校是否存在
//        School school = schoolService.getById(dto.getSchoolId());
//        if (school == null) {
//            throw new RuntimeException("学校不存在");
//        }
//
//        // 检查用户是否已经是教师
//        if (user.getRole() == RoleEnum.TEACHER) {
//            throw new RuntimeException("用户已经是教师，无需再次申请");
//        }
//
//        // 检查用户是否已有未处理的申请
//        long count = count(new QueryWrapper<TeacherApplication>()
//                .eq("uid", dto.getUid())
//                .eq("status", CertificationStatus.PENDING));
//        if (count > 0) {
//            throw new RuntimeException("用户已有未处理的教师申请");
//        }
//
//        // 创建申请
//        TeacherApplication application = new TeacherApplication();
//        application.setUid(dto.getUid());
//        application.setSchoolId(dto.getSchoolId()); // 直接使用 DTO 中的 schoolId
//        application.setRealName(dto.getRealName());
//        application.setProofImage(dto.getProofImage());
//        application.setStatus(CertificationStatus.PENDING);
//        save(application);
//    }
//}