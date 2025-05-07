package sdu.codeeducationplat.service.question;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import sdu.codeeducationplat.mapper.question.QuestionCodeMapper;
import sdu.codeeducationplat.model.question.QuestionCode;

@Service
public class QuestionCodeService extends ServiceImpl<QuestionCodeMapper, QuestionCode> {
}