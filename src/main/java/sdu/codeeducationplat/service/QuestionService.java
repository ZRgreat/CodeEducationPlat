package sdu.codeeducationplat.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sdu.codeeducationplat.dto.QuestionDTO;
import sdu.codeeducationplat.mapper.QuestionMapper;
import sdu.codeeducationplat.mapper.QuestionTagMapper;
import sdu.codeeducationplat.model.question.Question;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService extends ServiceImpl<QuestionMapper, Question> {

    private final QuestionChoiceService questionChoiceService;
    private final QuestionBlankService questionBlankService;
    private final QuestionCodeService questionCodeService;
    private final QuestionSubjectiveService questionSubjectiveService;
    private final QuestionTestCaseService questionTestCaseService;
    private final QuestionMapper questionMapper;
    private final QuestionTagMapper questionTagMapper;
    private final CategoryService categoryService;
    private final TagService tagService;



    @Transactional(rollbackFor = Exception.class)
    public Question createQuestion(QuestionDTO dto) {
        Question question = convertToEntity(dto);
        question.setIsDeleted(false); // 创建时设置为未删除
        this.save(question);
        log.info("Question created with ID: {}", question.getQuestionId());
        return question;
    }

    /**
     * 获取教师的题目列表
     *
     * @param authorUid 教师 UID
     * @return 题目列表
     */
    public List<Question> listQuestionsByAuthor(Long authorUid) {
        return this.list(
                new LambdaQueryWrapper<Question>()
                        .eq(Question::getAuthorUid, authorUid)
                        .eq(Question::getIsDeleted, false)
        );
    }

    /**
     * 更新题目
     */
    public Question updateQuestion(Long id, QuestionDTO dto) {
        Question existingQuestion = this.getById(id);
        if (existingQuestion == null || existingQuestion.getIsDeleted()) {
            throw new IllegalStateException("题目不存在或已被删除，id: " + id);
        }

        // 更新时保留原有 type，不允许修改
        Question updatedQuestion = convertToEntity(dto);
        updatedQuestion.setQuestionId(id);
        updatedQuestion.setType(existingQuestion.getType()); // 固定题型
        updatedQuestion.setIsDeleted(existingQuestion.getIsDeleted()); // 保留删除状态
        this.updateById(updatedQuestion);
        log.info("Question updated with ID: {}", id);
        return updatedQuestion;
    }

    /**
     * 删除题目（逻辑删除）
     */
    public void deleteQuestion(Long id, Long uid) {
        Question question = this.getById(id);
        if (question == null || question.getIsDeleted()) {
            throw new IllegalStateException("题目不存在或已被删除，id: " + id);
        }
        if (!question.getAuthorUid().equals(uid)) {
            throw new IllegalStateException("无权删除他人的题目，id: " + id);
        }
        question.setIsDeleted(true);
        this.updateById(question);
        log.info("Question deleted with ID: {}", id);
    }


    // DTO 转换为实体
    private Question convertToEntity(QuestionDTO dto) {
        Question question = new Question();
        question.setTitle(dto.getTitle());
        question.setDescription(dto.getDescription());
        question.setType(dto.getType());
        question.setScore(dto.getScore());
        question.setDifficulty(dto.getDifficulty());
        question.setAuthorUid(dto.getAuthorUid());
        question.setHints(dto.getHints());
        question.setIsPublic(dto.getIsPublic());
        question.setCategoryId(dto.getCategoryId());
        return question;
    }
}