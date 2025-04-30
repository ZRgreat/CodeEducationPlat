package sdu.codeeducationplat.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import sdu.codeeducationplat.common.Result;
import sdu.codeeducationplat.model.tag.Category;
import sdu.codeeducationplat.service.CategoryService;

@Tag(name = "管理员分类管理", description = "管理员对分类的管理接口")
@RestController
@RequestMapping("/api/admin/categories")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
public class AdminCategoryController {

    private final CategoryService categoryService;

    @Operation(summary = "添加分类", description = "管理员添加新分类")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Result<?> addCategory(
            @RequestParam String name,
            @RequestParam(required = false) Long parentId) {
        log.info("添加分类，name：{}，parentId：{}", name, parentId);
        Category category = new Category();
        category.setName(name);
        category.setParentId(parentId);
        categoryService.save(category);
        log.info("添加分类成功，name：{}", name);
        return Result.success("添加成功");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public Result<?> deleteCategory(@PathVariable Long id) {
        log.info("删除分类，id：{}", id);
        categoryService.removeById(id);
        log.info("删除分类成功，id：{}", id);
        return Result.success("删除成功");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Result<?> updateCategory(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam(required = false) Long parentId) {
        log.info("更新分类，id：{}，name：{}，parentId：{}", id, name, parentId);
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        category.setParentId(parentId);
        categoryService.updateById(category);
        log.info("更新分类成功，id：{}", id);
        return Result.success("更新成功");
    }

    @Operation(summary = "查询所有分类", description = "管理员查询所有分类")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Result<Page<Category>> getCategories(
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        log.info("获取分类列表，关键字：{}，页码：{}，每页大小：{}", keyword, page, size);
        Page<Category> result = categoryService.getCategoryPage(keyword, page, size);
        log.info("获取分类列表成功，返回分类数量：{}", result.getTotal());
        return Result.success(result);
    }
}