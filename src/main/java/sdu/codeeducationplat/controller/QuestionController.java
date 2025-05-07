package sdu.codeeducationplat.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sdu.codeeducationplat.common.Result;
import sdu.codeeducationplat.dto.question.QuestionDTO;
import sdu.codeeducationplat.dto.question.QuestionDetailResponse;
import sdu.codeeducationplat.dto.question.QuestionDslDTO;
import sdu.codeeducationplat.dto.question.QuestionQueryDTO;
import sdu.codeeducationplat.model.question.Question;
import sdu.codeeducationplat.service.question.QuestionService;
import sdu.codeeducationplat.service.user.TeacherService;

import java.util.List;

@Tag(name = "教师题目管理", description = "教师对题目的操作接口")
@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class QuestionController {

    private final TeacherService teacherService;
    private final QuestionService questionService;

    @Operation(summary = "创建题目", description = "创建新题目，需审核")
    @PostMapping
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public Result<Question> createQuestion(@Valid @RequestBody QuestionDTO dto, Authentication authentication) {
        log.info("教师创建题目，dto: {}", dto);
        Long uid = teacherService.validateTeacherExists(authentication);
        dto.setAuthorUid(uid);
        Question question = questionService.createFullQuestion(dto);
        return Result.success(question);
    }

    @Operation(summary = "通过 DSL 创建题目", description = "通过 DSL 创建新题目，需提供元信息")
    @PostMapping("/dsl")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public Result<Question> createQuestionFromDsl(
            @Valid @RequestBody QuestionDslDTO dslDto, Authentication authentication) {
        log.info("教师通过 DSL 创建题目，dslDto: {}", dslDto);
        Long uid = teacherService.validateTeacherExists(authentication);
        QuestionDTO dto = questionService.parseDslToDto(dslDto.getContent());
        dto.setTitle(dslDto.getTitle());
        dto.setDifficulty(dslDto.getDifficulty());
        dto.setCategoryIds(dslDto.getCategoryIds());
        dto.setAuthorUid(uid);
        dto.setHints(dslDto.getHints());
        dto.setIsPublic(dslDto.getIsPublic());
        Question question = questionService.createFullQuestion(dto);
        return Result.success(question);
    }

    @Operation(summary = "批量创建题目", description = "教师批量创建多个题目")
    @PostMapping("/batch")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public Result<List<Question>> batchCreateQuestions(
            @RequestBody List<QuestionDTO> dtos, Authentication authentication) {
        log.info("教师批量创建题目，数量: {}", dtos.size());
        Long uid = teacherService.validateTeacherExists(authentication);
        List<Question> questions = questionService.batchCreateQuestions(dtos, uid);
        return Result.success(questions);
    }

    @Operation(summary = "通过文件批量创建题目", description = "教师通过上传Excel或CSV文件批量创建题目")
    @PostMapping("/batch/upload")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public Result<List<Question>> batchCreateQuestionsByFile(
            @RequestParam("file") MultipartFile file, Authentication authentication) {
        log.info("教师通过文件批量创建题目，文件名: {}", file.getOriginalFilename());
        Long uid = teacherService.validateTeacherExists(authentication);
        List<QuestionDTO> dtos = questionService.parseFileToQuestionDTOs(file);
        List<Question> questions = questionService.batchCreateQuestions(dtos, uid);
        return Result.success(questions);
    }

    @Operation(summary = "更新题目", description = "教师更新已有题目，题型不可修改")
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public Result<Question> updateQuestion(@PathVariable Long id, @Valid @RequestBody QuestionDTO dto, Authentication authentication) {
        log.info("教师更新题目，id: {}, dto: {}", id, dto);
        Long uid = teacherService.validateTeacherExists(authentication);
        dto.setAuthorUid(uid);
        Question updatedQuestion = questionService.updateQuestion(id, dto);
        return Result.success(updatedQuestion);
    }

    @Operation(summary = "删除题目", description = "教师删除题目（逻辑删除）")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public Result<String> deleteQuestion(@PathVariable Long id, Authentication authentication) {
        log.info("教师删除题目，id: {}", id);
        Long uid = teacherService.validateTeacherExists(authentication);
        questionService.deleteQuestion(id, uid);
        return Result.success("删除成功");
    }

    @Operation(summary = "获取题目详情", description = "教师或学生获取题目详情")
    @GetMapping("/{id}")
    public Result<QuestionDetailResponse> getQuestionDetail(@PathVariable Long id) {
        log.info("获取题目详情，id: {}", id);
        QuestionDetailResponse response = questionService.getQuestionDetail(id);
        log.info("获取题目详情成功，id: {}", id);
        return Result.success(response);
    }

    @Operation(summary = "预览题目", description = "预览题目内容，不保存")
    @PostMapping("/preview")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public Result<QuestionDetailResponse> previewQuestion(
            @Valid @RequestBody QuestionDTO dto, Authentication authentication) {
        log.info("教师预览题目，dto: {}", dto);
        teacherService.validateTeacherExists(authentication);
        QuestionDetailResponse response = questionService.buildDetailFromDto(dto);
        return Result.success(response);
    }

    @Operation(summary = "通过 DSL 预览题目", description = "通过 DSL 预览题目内容，不保存")
    @PostMapping("/dsl/preview")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public Result<QuestionDetailResponse> previewQuestionFromDsl(
            @RequestBody String dslContent, Authentication authentication) {
        log.info("教师通过 DSL 预览题目，内容: {}", dslContent);
        teacherService.validateTeacherExists(authentication);
        QuestionDetailResponse response = questionService.previewQuestionFromDsl(dslContent);
        return Result.success(response);
    }

    @Operation(summary = "获取题目分页列表", description = "支持按题型、知识点、作者、审核状态筛选")
    @PostMapping("/list")
    @PreAuthorize("hasAnyRole('TEACHER','ADMIN')")
    public Result<IPage<Question>> listQuestions(
            @Valid @RequestBody QuestionQueryDTO queryDTO,
            Authentication authentication) {
        log.info("获取题目分页列表，queryDTO: {}", queryDTO);
        IPage<Question> questions = questionService.listQuestions(queryDTO, authentication);
        log.info("获取题目分页列表成功，返回题目数量: {}", questions.getTotal());
        return Result.success(questions);
    }
}