package sdu.codeeducationplat.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import sdu.codeeducationplat.dto.ApplyTeacherDTO;
import sdu.codeeducationplat.dto.TeacherApplicationDTO;
import sdu.codeeducationplat.mapper.TeacherApplicationMapper;
import sdu.codeeducationplat.mapper.SchoolMapper;
import sdu.codeeducationplat.model.enums.CertificationStatus;
import sdu.codeeducationplat.model.enums.RoleEnum;
import sdu.codeeducationplat.model.school.School;
import sdu.codeeducationplat.model.user.Teacher;
import sdu.codeeducationplat.model.user.TeacherApplication;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherApplicationService extends ServiceImpl<TeacherApplicationMapper, TeacherApplication> {

    private final UserRoleService userRoleService;
    private final TeacherService teacherService;
    private final TeacherApplicationMapper teacherApplicationMapper;
    private final SchoolMapper schoolMapper;

    /**
     * 判断当前用户是否已拥有教师权限
     * @param uid 用户uid
     * @return 教师是否存在
     */
    public boolean isTeacher(Long uid) {
        return teacherService.exists(new QueryWrapper<Teacher>().eq("uid", uid));
    }


    /** 判断当前用户是否已存在待审核的教师申请
     * @param uid 用户uid
     * @return 是否已有申请
     */
    public boolean hasPendingTeacherApplication(Long uid) {
        LambdaQueryWrapper<TeacherApplication> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(TeacherApplication::getUid, uid)
                .eq(TeacherApplication::getStatus, CertificationStatus.PENDING);
        return this.count(wrapper) > 0;
    }

    /**
     * 用户申请成为教师
     * @param uid 当前登录用户 UID
     * @param dto 申请信息
     */
    @Transactional(rollbackFor = Exception.class)
    public void applyTeacher(Long uid, ApplyTeacherDTO dto) {
        // 判断是否已是教师
        if (isTeacher(uid)) {
            throw new RuntimeException("您已是教师，无需重复申请");
        }

        // 判断是否已有待审核申请
        if (hasPendingTeacherApplication(uid)) {
            throw new RuntimeException("您已有一条待审核的教师申请");
        }

        // 保存教师申请记录
        TeacherApplication application = new TeacherApplication();
        application.setUid(uid);
        application.setSchoolId(dto.getSchoolId());
        application.setRealName(dto.getRealName());
        application.setProofImage(dto.getProofImage());
        application.setStatus(CertificationStatus.PENDING);
        application.setCreatedAt(LocalDateTime.now());

        save(application);
    }

    /**
     * 审核教师申请
     * @param applicationId 申请ID
     * @param approve 是否通过
     */
    @Transactional(rollbackFor = Exception.class)
    public void reviewApplication(Long applicationId, boolean approve) {
        TeacherApplication application = getById(applicationId);

        if (application == null) {
            throw new RuntimeException("申请不存在");
        }
        if (application.getStatus() != CertificationStatus.PENDING) {
            throw new RuntimeException("该申请已处理");
        }

        if (approve) {
            // 插入教师表
            teacherService.createTeacher(application.getUid(), application.getSchoolId(), application.getRealName());
            // 更新角色
            userRoleService.saveOrUpdateUserRole(application.getUid(), RoleEnum.TEACHER);
            application.setStatus(CertificationStatus.APPROVED);
        } else {
            application.setStatus(CertificationStatus.REJECTED);
        }
        updateById(application);
    }

    /**
     * 分页查询教师申请列表
     * @param keyword 搜索关键词（模糊匹配学校名称或教师姓名），可为空
     * @param status 申请状态（PENDING, APPROVED, REJECTED），可为空
     * @param page 当前页数
     * @param size 每页大小
     * @return 分页结果
     */
    public Page<TeacherApplicationDTO> getApplicationPage(String keyword, CertificationStatus status, int page, int size) {
        Page<TeacherApplication> applicationPage = new Page<>(page, size);

        LambdaQueryWrapper<TeacherApplication> wrapper = new LambdaQueryWrapper<>();
        if (status != null) {
            wrapper.eq(TeacherApplication::getStatus, status);
        }
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w
                    .like(TeacherApplication::getRealName, keyword)
                    .or()
                    .apply("s.name LIKE {0}", "%" + keyword + "%")
            );
        }

        // 使用 selectCountWithSchool 计算总数
        Long total = teacherApplicationMapper.selectCountWithSchool(wrapper);
        Page<TeacherApplication> records = teacherApplicationMapper.selectPageWithSchool(applicationPage, wrapper);
        records.setTotal(total);
        records.setPages((long) Math.ceil((double) total / size));

        List<TeacherApplicationDTO> dtoList = records.getRecords().stream().map(application -> {
            TeacherApplicationDTO dto = new TeacherApplicationDTO();
            BeanUtils.copyProperties(application, dto);
            return dto;
        }).collect(Collectors.toList());

        Page<TeacherApplicationDTO> dtoPage = new Page<>();
        dtoPage.setRecords(dtoList);
        dtoPage.setCurrent(records.getCurrent());
        dtoPage.setSize(records.getSize());
        dtoPage.setTotal(total);
        dtoPage.setPages((long) Math.ceil((double) total / size));

        return dtoPage;
    }

    public TeacherApplicationDTO getLatestApplication(Long uid) {
        TeacherApplication app = teacherApplicationMapper.selectLatestByUid(uid);
        if (app == null) return null;
        TeacherApplicationDTO dto = new TeacherApplicationDTO();
        BeanUtils.copyProperties(app, dto);
        School school = schoolMapper.selectById(app.getSchoolId());
        dto.setSchoolName(school != null ? school.getName() : "未知学校");
        return dto;
    }

    @Transactional
    public void updateTeacherApplication(Long uid, TeacherApplicationDTO dto) {
        TeacherApplication existing = teacherApplicationMapper.selectLatestByUid(uid);
        if (existing == null) {
            throw new RuntimeException("尚未提交教师申请，无法修改");
        }
        if (existing.getStatus() == CertificationStatus.APPROVED) {
            throw new RuntimeException("您的申请已通过，不能修改");
        }

        TeacherApplication updated = new TeacherApplication();
        updated.setApplicationId(existing.getApplicationId());
        updated.setUid(uid);
        updated.setSchoolId(dto.getSchoolId());
        updated.setRealName(dto.getRealName());
        updated.setProofImage(dto.getProofImage());
        updated.setStatus(CertificationStatus.PENDING); // 重置为待审核

        teacherApplicationMapper.updateById(updated);
    }

    public long countPendingApplications() {
        return teacherApplicationMapper.selectCount(
                new LambdaQueryWrapper<TeacherApplication>()
                        .eq(TeacherApplication::getStatus, CertificationStatus.PENDING)
        );
    }

}