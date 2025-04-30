package sdu.codeeducationplat.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sdu.codeeducationplat.common.Result;
import sdu.codeeducationplat.service.TagService;

@Tag(name = "管理员标签管理", description = "管理员对标签的管理接口")
@RestController
@RequestMapping("/api/admin/tags")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminTagController {

    private final TagService tagService;

    @Operation(summary = "添加标签", description = "管理员添加新标签")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> addTag(@RequestParam String name) {
        log.info("添加标签：{}", name);
        return tagService.addTag(name);
    }

    @Operation(summary = "删除标签", description = "管理员删除指定标签")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> deleteTag(@PathVariable Long id) {
        log.info("删除标签，id：{}", id);
        return tagService.deleteTag(id);
    }

    @Operation(summary = "更新标签", description = "管理员更新指定标签")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> updateTag(@PathVariable Long id, @RequestParam String name) {
        log.info("更新标签，id：{}，name：{}", id, name);
        return tagService.updateTag(id, name);
    }

    @Operation(summary = "查询标签列表", description = "分页查询标签列表，支持模糊搜索标签名称")
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public Result<?> listTags(@RequestParam(required = false) String keyword,
                              @RequestParam(defaultValue = "1") int page,
                              @RequestParam(defaultValue = "20") int size) {
        log.info("查询标签列表，关键字：{}，页码：{}，每页大小：{}", keyword, page, size);
        Page<sdu.codeeducationplat.model.tag.Tag> tags = tagService.searchTags(keyword, page, size);
        log.info("查询标签列表成功，返回标签数量：{}", tags.getTotal());
        return Result.success(tags);
    }
}