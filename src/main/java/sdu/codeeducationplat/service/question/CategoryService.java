package sdu.codeeducationplat.service.question;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import sdu.codeeducationplat.common.Result;
import sdu.codeeducationplat.mapper.course.CategoryMapper;
import sdu.codeeducationplat.model.question.Category;

@Service
@RequiredArgsConstructor
public class CategoryService extends ServiceImpl<CategoryMapper, Category> {

    private final CategoryMapper categoryMapper;

    @Transactional
    public Result<?> addCategory(String name, Long parentId) {
        if (exists(new LambdaQueryWrapper<Category>().eq(Category::getName, name))) {
            return Result.error("分类已存在");
        }
        if (parentId != null && getById(parentId) == null) { // 替换 existsById
            return Result.error("父分类不存在");
        }
        Category category = new Category();
        category.setName(name);
        category.setParentId(parentId);
        save(category);
        return Result.success(category);
    }

    @Transactional
    public Result<?> deleteCategory(Long id) {
        if (getById(id) == null) { // 替换 existsById
            return Result.error("分类不存在");
        }
        if (exists(new LambdaQueryWrapper<Category>().eq(Category::getParentId, id))) {
            return Result.error("请先删除子分类");
        }
        removeById(id);
        return Result.success("删除成功");
    }

    @Transactional
    public Result<?> updateCategory(Long id, String name, Long parentId) {
        Category category = getById(id); // 替换 existsById
        if (category == null) {
            return Result.error("分类不存在");
        }
        if (exists(new LambdaQueryWrapper<Category>().eq(Category::getName, name).ne(Category::getId, id))) {
            return Result.error("分类名称已存在");
        }
        if (parentId != null && getById(parentId) == null) { // 替换 existsById
            return Result.error("父分类不存在");
        }
        if (parentId != null && parentId.equals(id)) {
            return Result.error("父分类不能是自身");
        }
        category.setName(name);
        category.setParentId(parentId);
        updateById(category);
        return Result.success(category);
    }

    public Page<Category> searchCategories(String keyword, int page, int size) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Category::getName, keyword);
        }
        return this.page(new Page<>(page, size), wrapper);
    }

    public Page<Category> getCategoryPage(String keyword, int page, int size) {
        Page<Category> categoryPage = new Page<>(page, size);

        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Category::getName, keyword);
        }

        return categoryMapper.selectPage(categoryPage, wrapper);
    }
}