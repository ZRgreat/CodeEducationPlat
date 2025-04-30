package sdu.codeeducationplat.mapper;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import sdu.codeeducationplat.model.user.TeacherApplication;
import com.baomidou.mybatisplus.core.toolkit.Constants;

public interface TeacherApplicationMapper extends BaseMapper<TeacherApplication> {
    Long selectCountWithSchool(@Param(Constants.WRAPPER) Wrapper<TeacherApplication> wrapper);
    TeacherApplication selectLatestByUid(@Param("uid") Long uid);
    Page<TeacherApplication> selectPageWithSchool(Page<TeacherApplication> page, @Param(Constants.WRAPPER) Wrapper<TeacherApplication> wrapper);
}
