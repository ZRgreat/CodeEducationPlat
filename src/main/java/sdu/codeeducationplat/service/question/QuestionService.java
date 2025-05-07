package sdu.codeeducationplat.service.question;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import sdu.codeeducationplat.dto.question.*;
import sdu.codeeducationplat.mapper.course.CategoryMapper;
import sdu.codeeducationplat.mapper.question.QuestionCategoryMapper;
import sdu.codeeducationplat.mapper.question.QuestionMapper;
import sdu.codeeducationplat.model.enums.CertificationStatus;
import sdu.codeeducationplat.model.enums.QuestionType;
import sdu.codeeducationplat.model.question.*;
import sdu.codeeducationplat.util.DslParser;
import sdu.codeeducationplat.util.ExcelParser;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class QuestionService extends ServiceImpl<QuestionMapper, Question> {

    private final QuestionMapper questionMapper;
    private final CategoryMapper categoryMapper;
    private final QuestionCategoryMapper questionCategoryMapper;
    private final QuestionCodeService questionCodeService;
    private final QuestionTestCaseService questionTestCaseService;
    private final QuestionChoiceService questionChoiceService;
    private final QuestionBlankService questionBlankService;
    private final QuestionSubjectiveService questionSubjectiveService;

    @Transactional(rollbackFor = Exception.class)
    public Question createQuestion(QuestionDTO dto) {
        if (dto.getCategoryIds() != null) {
            for (Long categoryId : dto.getCategoryIds()) {
                if (categoryMapper.selectById(categoryId) == null) {
                    throw new IllegalArgumentException("分类不存在，ID: " + categoryId);
                }
            }
        }

        Question question = new Question();
        BeanUtils.copyProperties(dto, question, "content");
        question.setDescription(dto.getContent());
        question.setIsDeleted(false);
        question.setStatus(CertificationStatus.PENDING); // 默认待审核
        questionMapper.insert(question);

        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            for (Long categoryId : dto.getCategoryIds()) {
                QuestionCategory qc = new QuestionCategory();
                qc.setQuestionId(question.getQuestionId());
                qc.setCategoryId(categoryId);
                questionCategoryMapper.insert(qc);
            }
        }

        log.info("Question created with ID: {}", question.getQuestionId());
        return question;
    }

    @Transactional(rollbackFor = Exception.class)
    public Question createFullQuestion(QuestionDTO dto) {
        if (dto.getContent() != null && !dto.getContent().isEmpty() && dto.getChoices() == null && dto.getBlanks() == null &&
                dto.getCodes() == null && dto.getSubjective() == null && dto.getTestCases() == null && dto.getAnswer() == null) {
            QuestionDTO parsedDto = parseDslToDto(dto.getContent());
            parsedDto.setTitle(dto.getTitle());
            parsedDto.setDifficulty(dto.getDifficulty());
            parsedDto.setCategoryIds(dto.getCategoryIds());
            parsedDto.setAuthorUid(dto.getAuthorUid());
            parsedDto.setHints(dto.getHints());
            parsedDto.setIsPublic(dto.getIsPublic());
            parsedDto.setScore(dto.getScore());
            parsedDto.setType(dto.getType());
            dto = parsedDto;
        }

        Question question = createQuestion(dto);
        saveAdditionalData(question.getQuestionId(), dto);
        return question;
    }

    public QuestionDTO parseDslToDto(String dslContent) {
        return DslParser.parse(dslContent);
    }

    @Transactional(rollbackFor = Exception.class)
    public QuestionDetailResponse previewQuestionFromDsl(String dslContent) {
        QuestionDTO dto = parseDslToDto(dslContent);
        return buildDetailFromDto(dto);
    }

    private void saveAdditionalData(Long questionId, QuestionDTO dto) {
        if (dto.getCodes() != null && !dto.getCodes().isEmpty()) {
            if (!List.of(QuestionType.PROGRAM, QuestionType.CODE_BLANK, QuestionType.FUNCTION).contains(dto.getType())) {
                throw new IllegalArgumentException("非编程题不能包含代码框架");
            }
            questionCodeService.remove(new LambdaQueryWrapper<QuestionCode>().eq(QuestionCode::getQuestionId, questionId));
            for (QuestionCodeDTO codeDTO : dto.getCodes()) {
                QuestionCode code = new QuestionCode();
                BeanUtils.copyProperties(codeDTO, code);
                code.setQuestionId(questionId);
                questionCodeService.save(code);
            }
        }

        if (dto.getTestCases() != null && !dto.getTestCases().isEmpty()) {
            if (!List.of(QuestionType.PROGRAM, QuestionType.CODE_BLANK, QuestionType.FUNCTION).contains(dto.getType())) {
                throw new IllegalArgumentException("非编程题不能包含测试用例");
            }
            questionTestCaseService.remove(new LambdaQueryWrapper<QuestionTestCase>().eq(QuestionTestCase::getQuestionId, questionId));
            for (QuestionTestCaseDTO tcDTO : dto.getTestCases()) {
                QuestionTestCase tc = new QuestionTestCase();
                BeanUtils.copyProperties(tcDTO, tc);
                tc.setQuestionId(questionId);
                questionTestCaseService.save(tc);
            }
        }

        if (dto.getChoices() != null && !dto.getChoices().isEmpty()) {
            if (!List.of(QuestionType.SINGLE, QuestionType.MULTI).contains(dto.getType())) {
                throw new IllegalArgumentException("非选择题不能包含选项");
            }
            questionChoiceService.remove(new LambdaQueryWrapper<QuestionChoice>().eq(QuestionChoice::getQuestionId, questionId));
            for (QuestionChoiceDTO choiceDTO : dto.getChoices()) {
                QuestionChoice choice = new QuestionChoice();
                BeanUtils.copyProperties(choiceDTO, choice);
                choice.setQuestionId(questionId);
                questionChoiceService.save(choice);
            }
        }

        if (dto.getBlanks() != null && !dto.getBlanks().isEmpty()) {
            if (!dto.getType().equals(QuestionType.BLANK)) {
                throw new IllegalArgumentException("非填空题不能包含答案");
            }
            questionBlankService.remove(new LambdaQueryWrapper<QuestionBlank>().eq(QuestionBlank::getQuestionId, questionId));
            for (QuestionBlankDTO blankDTO : dto.getBlanks()) {
                QuestionBlank blank = new QuestionBlank();
                BeanUtils.copyProperties(blankDTO, blank);
                blank.setQuestionId(questionId);
                questionBlankService.save(blank);
            }
        }

        if (dto.getSubjective() != null) {
            if (!dto.getType().equals(QuestionType.SUBJECTIVE)) {
                throw new IllegalArgumentException("非主观题不能包含主观题信息");
            }
            questionSubjectiveService.remove(new LambdaQueryWrapper<QuestionSubjective>().eq(QuestionSubjective::getQuestionId, questionId));
            QuestionSubjective subjective = new QuestionSubjective();
            BeanUtils.copyProperties(dto.getSubjective(), subjective);
            subjective.setQuestionId(questionId);
            questionSubjectiveService.save(subjective);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public Question updateQuestion(Long id, QuestionDTO dto) {
        Question existingQuestion = this.getById(id);
        if (existingQuestion == null || existingQuestion.getIsDeleted()) {
            throw new IllegalStateException("题目不存在或已被删除，id: " + id);
        }
        if (!existingQuestion.getType().equals(dto.getType())) {
            throw new IllegalArgumentException("题型不可修改");
        }
        BeanUtils.copyProperties(dto, existingQuestion, "questionId", "type", "isDeleted", "content");
        existingQuestion.setDescription(dto.getContent());
        this.updateById(existingQuestion);

        questionCategoryMapper.delete(new LambdaQueryWrapper<QuestionCategory>().eq(QuestionCategory::getQuestionId, id));
        if (dto.getCategoryIds() != null && !dto.getCategoryIds().isEmpty()) {
            for (Long categoryId : dto.getCategoryIds()) {
                QuestionCategory qc = new QuestionCategory();
                qc.setQuestionId(id);
                qc.setCategoryId(categoryId);
                questionCategoryMapper.insert(qc);
            }
        }
        saveAdditionalData(id, dto);
        return existingQuestion;
    }

    @Transactional(rollbackFor = Exception.class)
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

    @Transactional(rollbackFor = Exception.class)
    public QuestionDetailResponse getQuestionDetail(Long id) {
        Question question = this.getById(id);
        if (question == null || question.getIsDeleted()) {
            throw new IllegalStateException("题目不存在或已被删除，id: " + id);
        }
        if (!question.getStatus().equals(CertificationStatus.APPROVED)) {
            throw new IllegalStateException("题目未通过审核，id: " + id);
        }

        QuestionDetailResponse response = new QuestionDetailResponse();
        response.setQuestion(question);

        switch (question.getType()) {
            case PROGRAM:
            case CODE_BLANK:
            case FUNCTION:
                response.setCodes(questionCodeService.list(new LambdaQueryWrapper<QuestionCode>().eq(QuestionCode::getQuestionId, id)));
                response.setTestCases(questionTestCaseService.list(new LambdaQueryWrapper<QuestionTestCase>().eq(QuestionTestCase::getQuestionId, id)));
                break;
            case SINGLE:
            case MULTI:
                response.setChoices(questionChoiceService.list(new LambdaQueryWrapper<QuestionChoice>().eq(QuestionChoice::getQuestionId, id)));
                break;
            case BLANK:
                response.setBlanks(questionBlankService.list(new LambdaQueryWrapper<QuestionBlank>().eq(QuestionBlank::getQuestionId, id)));
                break;
            case SUBJECTIVE:
                response.setSubjective(questionSubjectiveService.getOne(new LambdaQueryWrapper<QuestionSubjective>().eq(QuestionSubjective::getQuestionId, id)));
                break;
            case JUDGMENT:
                response.setAnswer(question.getAnswer());
                break;
            default:
                log.warn("未知的题目类型: {}", question.getType());
        }
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public List<Question> batchCreateQuestions(List<QuestionDTO> dtos, Long authorUid) {
        List<Question> questions = new ArrayList<>();
        for (QuestionDTO dto : dtos) {
            dto.setAuthorUid(authorUid);
            Question question = createFullQuestion(dto);
            questions.add(question);
        }
        return questions;
    }

    public List<QuestionDTO> parseFileToQuestionDTOs(MultipartFile file) {
        return ExcelParser.parseFile(file);
    }

    public QuestionDetailResponse buildDetailFromDto(QuestionDTO dto) {
        QuestionDetailResponse response = new QuestionDetailResponse();
        Question question = new Question();
        BeanUtils.copyProperties(dto, question, "content");
        question.setDescription(dto.getContent());
        response.setQuestion(question);

        switch (dto.getType()) {
            case PROGRAM:
            case CODE_BLANK:
            case FUNCTION:
                response.setCodes(dto.getCodes() != null ? dto.getCodes().stream().map(codeDTO -> {
                    QuestionCode code = new QuestionCode();
                    BeanUtils.copyProperties(codeDTO, code);
                    return code;
                }).toList() : new ArrayList<>());
                response.setTestCases(dto.getTestCases() != null ? dto.getTestCases().stream().map(tcDTO -> {
                    QuestionTestCase tc = new QuestionTestCase();
                    BeanUtils.copyProperties(tcDTO, tc);
                    return tc;
                }).toList() : new ArrayList<>());
                break;
            case SINGLE:
            case MULTI:
                response.setChoices(dto.getChoices() != null ? dto.getChoices().stream().map(choiceDTO -> {
                    QuestionChoice choice = new QuestionChoice();
                    BeanUtils.copyProperties(choiceDTO, choice);
                    return choice;
                }).toList() : new ArrayList<>());
                break;
            case BLANK:
                response.setBlanks(dto.getBlanks() != null ? dto.getBlanks().stream().map(blankDTO -> {
                    QuestionBlank blank = new QuestionBlank();
                    BeanUtils.copyProperties(blankDTO, blank);
                    return blank;
                }).toList() : new ArrayList<>());
                break;
            case SUBJECTIVE:
                if (dto.getSubjective() != null) {
                    QuestionSubjective subjective = new QuestionSubjective();
                    BeanUtils.copyProperties(dto.getSubjective(), subjective);
                    response.setSubjective(subjective);
                }
                break;
            case JUDGMENT:
                response.setAnswer(dto.getAnswer());
                break;
            default:
                log.warn("未知的题目类型: {}", dto.getType());
        }
        return response;
    }

    @Transactional(rollbackFor = Exception.class)
    public IPage<Question> listQuestions(QuestionQueryDTO queryDTO, Authentication authentication) {
        // 分页参数校验
        if (queryDTO.getPageNum() == null || queryDTO.getPageNum() < 1) queryDTO.setPageNum(1);
        if (queryDTO.getPageSize() == null || queryDTO.getPageSize() < 1) queryDTO.setPageSize(5);
        Page<Question> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        // 构建查询条件
        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getIsDeleted, false);

        // 用户认证与角色判断
        final Long currentUserUid;
        boolean isAdmin = false;
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof Long) {
                currentUserUid = (Long) principal;
                isAdmin = authentication.getAuthorities().stream()
                        .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));
            } else {
                log.error("无效的 principal 类型: {}", principal != null ? principal.getClass().getName() : "null");
                throw new IllegalStateException("无法解析用户 ID");
            }
        } else {
            log.error("用户未登录或认证失败");
            throw new IllegalStateException("用户未登录");
        }

        // 权限控制
        if (!isAdmin && queryDTO.getAuthorUid() == null && queryDTO.getIsPublic() == null) {
            wrapper.eq(Question::getAuthorUid, currentUserUid);
        }

        // 筛选条件
        if (queryDTO.getType() != null) {
            wrapper.eq(Question::getType, queryDTO.getType());
        }
        if (queryDTO.getAuthorUid() != null) {
            wrapper.eq(Question::getAuthorUid, queryDTO.getAuthorUid());
        }
        if (queryDTO.getStatus() != null) {
            wrapper.eq(Question::getStatus, queryDTO.getStatus());
        }
        if (queryDTO.getIsPublic() != null) {
            wrapper.eq(Question::getIsPublic, queryDTO.getIsPublic());
            if (queryDTO.getIsPublic()) {
                wrapper.eq(Question::getStatus, CertificationStatus.APPROVED);
            }
        }
        if (queryDTO.getTitle() != null && !queryDTO.getTitle().trim().isEmpty()) {
            wrapper.like(Question::getTitle, queryDTO.getTitle().trim());
        }
        if (queryDTO.getDifficulty() != null) {
            wrapper.eq(Question::getDifficulty, queryDTO.getDifficulty());
        }

        // 排序
        wrapper.orderByDesc(Question::getCreatedAt);

        // 执行查询
        IPage<Question> result = queryDTO.getCategoryId() != null
                ? questionMapper.selectPageWithCategory(page, wrapper, queryDTO.getCategoryId())
                : baseMapper.selectPage(page, wrapper);

        log.info("查询题目列表成功，用户: {}, 是管理员: {}, 总条数: {}", currentUserUid, isAdmin, result.getTotal());
        return result;
    }

    @Transactional(rollbackFor = Exception.class)
    public void reviewQuestion(Long id, CertificationStatus status, String reviewComment) {
        Question question = this.getById(id);
        if (question == null || question.getIsDeleted()) {
            throw new IllegalStateException("题目不存在或已被删除，id: " + id);
        }
        question.setStatus(status);
        question.setReviewComment(reviewComment);
        this.updateById(question);
        log.info("题目审核完成，id: {}, 状态: {}, 意见: {}", id, status, reviewComment);
    }

    @Transactional(rollbackFor = Exception.class)
    public IPage<Question> listPendingQuestions(QuestionQueryDTO queryDTO) {
        if (queryDTO.getPageNum() == null || queryDTO.getPageNum() < 1) queryDTO.setPageNum(1);
        if (queryDTO.getPageSize() == null || queryDTO.getPageSize() < 1) queryDTO.setPageSize(5);
        Page<Question> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());

        LambdaQueryWrapper<Question> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Question::getIsDeleted, false);
        wrapper.eq(Question::getStatus, CertificationStatus.PENDING);
        if (queryDTO.getType() != null) {
            wrapper.eq(Question::getType, queryDTO.getType());
        }
        if (queryDTO.getCategoryId() != null) {
            wrapper.inSql(Question::getQuestionId,
                    "SELECT question_id FROM question_category WHERE category_id = #{categoryId}");
        }
        if (queryDTO.getTitle() != null && !queryDTO.getTitle().trim().isEmpty()) {
            wrapper.like(Question::getTitle, queryDTO.getTitle().trim());
        }
        if (queryDTO.getDifficulty() != null) {
            wrapper.eq(Question::getDifficulty, queryDTO.getDifficulty());
        }

        wrapper.orderByDesc(Question::getCreatedAt);
        IPage<Question> result = questionMapper.selectPageWithCategory(page, wrapper, queryDTO.getCategoryId());
        log.info("查询待审核题目列表成功，总条数: {}", result.getTotal());
        return result;
    }
}