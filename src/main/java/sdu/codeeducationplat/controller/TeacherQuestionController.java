package sdu.codeeducationplat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import sdu.codeeducationplat.common.Result;
import sdu.codeeducationplat.dto.QuestionDTO;
import sdu.codeeducationplat.model.enums.QuestionType;
import sdu.codeeducationplat.model.question.*;
import sdu.codeeducationplat.service.*;
import java.util.List;

@Tag(name = "教师题目管理", description = "教师对题目的操作接口")
@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class TeacherQuestionController {

    private final QuestionService questionService;
    private final QuestionCodeService questionCodeService;
    private final QuestionTestCaseService questionTestCaseService;
    private final QuestionChoiceService questionChoiceService;
    private final TeacherService teacherService;

    @Data
    public static class QuestionDetailResponse {
        private Question question;
        private QuestionCode code;
        private List<QuestionTestCase> testCases;
        private List<QuestionChoice> choices;
        private List<QuestionBlank> blanks;
        private QuestionSubjective subjective;
    }

    @Operation(summary = "创建题目", description = "教师创建新题目")
    @PostMapping("/questions")
    @PreAuthorize("hasRole('TEACHER')")
    public Result<Question> createQuestion(@RequestBody QuestionDTO dto, Authentication authentication) {
        log.info("教师创建题目，dto: {}", dto);
        Long uid = teacherService.validateTeacherExists(authentication);
        dto.setAuthorUid(uid);
        Question question = questionService.createQuestion(dto);
        return Result.success(question);
    }

    @Operation(summary = "更新题目", description = "教师更新已有题目，题型不可修改")
    @PutMapping("/questions/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public Result<Question> updateQuestion(@PathVariable Long id, @RequestBody QuestionDTO dto, Authentication authentication) {
        log.info("教师更新题目，id: {}, dto: {}", id, dto);
        Long uid = teacherService.validateTeacherExists(authentication);
        dto.setAuthorUid(uid);
        Question updatedQuestion = questionService.updateQuestion(id, dto);
        return Result.success(updatedQuestion);
    }

    @Operation(summary = "删除题目", description = "教师删除题目（逻辑删除）")
    @DeleteMapping("/questions/{id}")
    @PreAuthorize("hasRole('TEACHER')")
    public Result<String> deleteQuestion(@PathVariable Long id, Authentication authentication) {
        log.info("教师删除题目，id: {}", id);
        Long uid = teacherService.validateTeacherExists(authentication);
        questionService.deleteQuestion(id, uid);
        return Result.success("删除成功");
    }

    @Operation(summary = "获取题目列表", description = "获取教师创建的题目列表")
    @GetMapping("/questions")
    @PreAuthorize("hasRole('TEACHER')")
    public Result<List<Question>> listQuestions(Authentication authentication) {
        log.info("获取题目列表");
        Long uid = teacherService.validateTeacherExists(authentication);
        List<Question> questions = questionService.listQuestionsByAuthor(uid);
        log.info("获取题目列表成功，返回题目数量: {}", questions.size());
        return Result.success(questions);
    }

    @Operation(summary = "获取题目详情", description = "教师或学生获取题目详情")
    @GetMapping("/questions/{id}")
    public Result<QuestionDetailResponse> getQuestionDetail(@PathVariable Long id) {
        log.info("获取题目详情，id: {}", id);
        Question question = questionService.getById(id);
        if (question == null || question.getIsDeleted()) {
            log.warn("题目不存在或已被删除，id: {}", id);
            throw new IllegalStateException("题目不存在或已被删除");
        }

        QuestionDetailResponse response = new QuestionDetailResponse();
        response.setQuestion(question);

        if (question.getType() == QuestionType.PROGRAM || question.getType() == QuestionType.CODE_BLANK || question.getType() == QuestionType.FUNCTION) {
            QuestionCode code = questionCodeService.getOne(
                    new LambdaQueryWrapper<QuestionCode>().eq(QuestionCode::getQuestionId, id)
            );
            List<QuestionTestCase> testCases = questionTestCaseService.list(
                    new LambdaQueryWrapper<QuestionTestCase>().eq(QuestionTestCase::getQuestionId, id)
            );
            response.setCode(code);
            response.setTestCases(testCases);
        } else if (question.getType() == QuestionType.SINGLE || question.getType() == QuestionType.MULTI) {
            List<QuestionChoice> choices = questionChoiceService.list(
                    new LambdaQueryWrapper<QuestionChoice>().eq(QuestionChoice::getQuestionId, id)
            );
            response.setChoices(choices);
        }

        log.info("获取题目详情成功，id: {}", id);
        return Result.success(response);
    }
}