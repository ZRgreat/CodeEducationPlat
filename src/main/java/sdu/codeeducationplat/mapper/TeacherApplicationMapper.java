package sdu.codeeducationplat.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import sdu.codeeducationplat.model.TeacherApplication;
import com.baomidou.mybatisplus.core.toolkit.Constants;

public interface TeacherApplicationMapper extends BaseMapper<TeacherApplication> {

    Page<TeacherApplication> selectPageWithSchool(Page<TeacherApplication> page, @Param(Constants.WRAPPER) Wrapper<TeacherApplication> wrapper);
}
