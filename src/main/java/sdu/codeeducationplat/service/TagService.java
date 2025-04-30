package sdu.codeeducationplat.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import sdu.codeeducationplat.common.Result;
import sdu.codeeducationplat.mapper.TagMapper;
import sdu.codeeducationplat.model.tag.Tag;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService extends ServiceImpl<TagMapper, Tag> {

    private final TagMapper tagMapper;

    @Transactional
    public Result<?> addTag(String name) {
        if (exists(new LambdaQueryWrapper<Tag>().eq(Tag::getName, name))) {
            return Result.error("标签已存在");
        }
        Tag tag = new Tag();
        tag.setName(name);
        save(tag);
        return Result.success(tag);
    }

    @Transactional
    public Result<?> deleteTag(Long id) {
        if (getById(id) == null) { // 替换 existsById
            return Result.error("标签不存在");
        }
        removeById(id);
        return Result.success("删除成功");
    }

    @Transactional
    public Result<?> updateTag(Long id, String name) {
        Tag tag = getById(id); // 替换 existsById
        if (tag == null) {
            return Result.error("标签不存在");
        }
        if (exists(new LambdaQueryWrapper<Tag>().eq(Tag::getName, name).ne(Tag::getId, id))) {
            return Result.error("标签名称已存在");
        }
        tag.setName(name);
        updateById(tag);
        return Result.success(tag);
    }

    public Result<?> listTags() {
        List<Tag> tags = list();
        return Result.success(tags);
    }

    public Page<Tag> searchTags(String keyword, int page, int size) {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            wrapper.like(Tag::getName, keyword);
        }
        return this.page(new Page<>(page, size), wrapper);
    }
}