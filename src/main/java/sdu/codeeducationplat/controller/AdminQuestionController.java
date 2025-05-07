package sdu.codeeducationplat.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sdu.codeeducationplat.common.Result;
import sdu.codeeducationplat.dto.question.QuestionQueryDTO;
import sdu.codeeducationplat.dto.question.ReviewRequestDTO;
import sdu.codeeducationplat.model.question.Question;
import sdu.codeeducationplat.service.question.QuestionService;

@Tag(name = "管理员题目审核", description = "管理员对题目的审核操作接口")
@RestController
@RequestMapping("/api/admin/questions")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminQuestionController {

    private final QuestionService questionService;

    @Operation(summary = "审核题目", description = "管理员审核题目，设置状态和审核意见")
    @PutMapping("/{id}/review")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<String> reviewQuestion(
            @PathVariable Long id,
            @RequestBody ReviewRequestDTO reviewDTO) {
        log.info("管理员审核题目，id: {}, 状态: {}, 意见: {}", id, reviewDTO.getStatus(), reviewDTO.getReviewComment());
        questionService.reviewQuestion(id, reviewDTO.getStatus(), reviewDTO.getReviewComment());
        return Result.success("审核完成");
    }

    @Operation(summary = "获取待审核题目列表", description = "分页获取待审核题目")
    @PostMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<IPage<Question>> listPendingQuestions(
            @Valid @RequestBody QuestionQueryDTO queryDTO) {
        log.info("获取待审核题目列表，queryDTO: {}", queryDTO);
        IPage<Question> questions = questionService.listPendingQuestions(queryDTO);
        log.info("获取待审核题目列表成功，返回题目数量: {}", questions.getTotal());
        return Result.success(questions);
    }
}