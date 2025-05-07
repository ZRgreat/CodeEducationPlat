package sdu.codeeducationplat.mapper.question;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;
import sdu.codeeducationplat.model.question.Question;

public interface QuestionMapper extends BaseMapper<Question> {
    IPage<Question> selectPageWithCategory(
            @Param("page") Page<Question> page,
            @Param("ew") com.baomidou.mybatisplus.core.conditions.Wrapper<Question> wrapper,
            @Param("categoryId") Long categoryId
    );
}