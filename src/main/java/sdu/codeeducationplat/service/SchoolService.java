package sdu.codeeducationplat.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import sdu.codeeducationplat.dto.SchoolDTO;
import sdu.codeeducationplat.mapper.SchoolMapper;
import sdu.codeeducationplat.model.school.School;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SchoolService extends ServiceImpl<SchoolMapper, School> {

    private final SchoolMapper schoolMapper;
    /**
     * 添加学校信息
     * @param dto 添加请求
     * @throws RuntimeException 如果学校已存在
     */
    @Transactional(rollbackFor = Exception.class)
    public School addSchool(SchoolDTO dto) {
        if (getOne(new QueryWrapper<School>().eq("code", dto.getCode())) != null) {
            throw new RuntimeException("学校代码已存在");
        }
        // 检查 name 是否已存在
        if (getOne(new QueryWrapper<School>().eq("name", dto.getName())) != null) {
            throw new RuntimeException("学校名称已存在");
        }
        School school = new School();
        school.setName(dto.getName());
        school.setCode(dto.getCode());
        save(school);
        return school;
    }

    /**
     * @param keyword 查询的关键字，匹配学校名称或者代码
     * @param page  当前页码，从1开始
     * @param size  每页条数（分页大小）
     * @return Page<School>	分页结果，包含总条数、总页数、当前页数据列表等
     */
    public Page<School> searchSchools(String keyword, int page, int size) {
        LambdaQueryWrapper<School> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(School::getName, keyword).or().like(School::getCode, keyword);
        }
        return page(new Page<>(page, size), wrapper);
    }

    /**
     * 删除学校
     * @param schoolId
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteSchool(Long schoolId) {
        School school = getById(schoolId);
        if (school == null) {
            throw new RuntimeException("学校不存在");
        }
        this.removeById(schoolId);
    }


    /**
     * @param keyword 查询的关键字，匹配学校名称或者代码
     * @return 学校列表
     */
    public List<School> listSchoolsByKeyword(String keyword) {
        LambdaQueryWrapper<School> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(School::getName, keyword).or().like(School::getCode, keyword);
        }
        return list(wrapper);
    }

    public Page<School> getSchoolPage(String keyword, int page, int size) {
        Page<School> schoolPage = new Page<>(page, size);

        LambdaQueryWrapper<School> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w
                    .like(School::getName, keyword)
                    .or()
                    .like(School::getCode, keyword)
            );
        }

        return schoolMapper.selectPage(schoolPage, wrapper);
    }


}