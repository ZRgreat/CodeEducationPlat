//package sdu.codeeducationplat.service;
//
//import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import sdu.codeeducationplat.mapper.TeacherMapper;
//import sdu.codeeducationplat.model.Teacher;
//import sdu.codeeducationplat.model.TeacherApplication;
//import sdu.codeeducationplat.model.User;
//import sdu.codeeducationplat.model.enums.CertificationStatus;
//import sdu.codeeducationplat.model.enums.RoleEnum;
//
//@Service
//public class TeacherService extends ServiceImpl<TeacherMapper, Teacher> {
//
//    @Autowired
//    private TeacherApplicationService teacherApplicationService;
//
//    @Autowired
//    private UserService userService;
//
//    public void approveApplication(Long applicationId) {
//        // 获取申请
//        TeacherApplication application = teacherApplicationService.getById(applicationId);
//        if (application == null) {
//            throw new RuntimeException("申请不存在");
//        }
//        if (application.getStatus() != CertificationStatus.PENDING) {
//            throw new RuntimeException("申请已处理");
//        }
//
//        // 更新申请状态
//        application.setStatus(CertificationStatus.APPROVED);
//        teacherApplicationService.updateById(application);
//
//        // 创建教师
//        Teacher teacher = new Teacher();
//        teacher.setUid(application.getUid());
//        teacher.setSchoolId(application.getSchoolId());
//        teacher.setRealName(application.getRealName());
//        save(teacher);
//
//        // 更新用户角色为教师
//        User user = userService.getById(application.getUid());
//        user.setRole(RoleEnum.TEACHER);
//        userService.updateById(user);
//    }
//
//    public void rejectApplication(Long applicationId) {
//        // 获取申请
//        TeacherApplication application = teacherApplicationService.getById(applicationId);
//        if (application == null) {
//            throw new RuntimeException("申请不存在");
//        }
//        if (application.getStatus() != CertificationStatus.PENDING) {
//            throw new RuntimeException("申请已处理");
//        }
//
//        // 更新申请状态为 rejected
//        application.setStatus(CertificationStatus.REJECTED);
//        teacherApplicationService.updateById(application);
//    }
//}