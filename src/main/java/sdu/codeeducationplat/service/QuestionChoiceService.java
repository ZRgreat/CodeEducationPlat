package sdu.codeeducationplat.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import sdu.codeeducationplat.mapper.QuestionChoiceMapper;
import sdu.codeeducationplat.model.question.QuestionChoice;

@Service
public class QuestionChoiceService extends ServiceImpl<QuestionChoiceMapper, QuestionChoice> {
}