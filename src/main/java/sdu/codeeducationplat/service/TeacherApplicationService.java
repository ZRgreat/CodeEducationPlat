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
import sdu.codeeducationplat.model.*;
import sdu.codeeducationplat.model.enums.CertificationStatus;
import sdu.codeeducationplat.model.enums.RoleEnum;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeacherApplicationService extends ServiceImpl<TeacherApplicationMapper, TeacherApplication> {

    private final UserRoleService userRoleService;
    private final TeacherService teacherService;
    private final TeacherApplicationMapper teacherApplicationMapper;

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

        Page<TeacherApplication> records = teacherApplicationMapper.selectPageWithSchool(applicationPage, wrapper);

        List<TeacherApplicationDTO> dtoList = records.getRecords().stream().map(application -> {
            TeacherApplicationDTO dto = new TeacherApplicationDTO();
            BeanUtils.copyProperties(application, dto);
            return dto;
        }).collect(Collectors.toList());

        Page<TeacherApplicationDTO> dtoPage = new Page<>();
        dtoPage.setRecords(dtoList);
        dtoPage.setCurrent(records.getCurrent());
        dtoPage.setSize(records.getSize());
        dtoPage.setTotal(records.getTotal());
        dtoPage.setPages(records.getPages());

        return dtoPage;
    }

}