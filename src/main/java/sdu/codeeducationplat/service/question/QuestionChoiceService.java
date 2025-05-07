package sdu.codeeducationplat.service.question;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import sdu.codeeducationplat.mapper.question.QuestionChoiceMapper;
import sdu.codeeducationplat.model.question.QuestionChoice;

@Service
public class QuestionChoiceService extends ServiceImpl<QuestionChoiceMapper, QuestionChoice> {
}