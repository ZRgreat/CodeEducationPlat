package sdu.codeeducationplat.service.question;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import sdu.codeeducationplat.mapper.question.QuestionCategoryMapper;
import sdu.codeeducationplat.model.question.QuestionCategory;

@Service
@RequiredArgsConstructor
public class QuestionCategoryService extends ServiceImpl<QuestionCategoryMapper, QuestionCategory> {

}