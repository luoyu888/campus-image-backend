package com.campus.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.demo.common.Result;
import com.campus.demo.dto.TagDTO;
import com.campus.demo.entity.Image;
import com.campus.demo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/image")
@CrossOrigin
public class ImageController {

    @Autowired
    private ImageService imageService;

    /**
     * 上传影像
     */
    @PostMapping("/upload")
    public Result<String> uploadImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam("category") String category,
            @RequestParam("uploaderId") Long uploaderId) {
        try {
            String url = imageService.uploadImage(file, category, uploaderId);
            return Result.success(url);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("上传失败：" + e.getMessage());
        }
    }

    /**
     * 获取影像列表（分页）
     */
    @GetMapping("/list")
    public Result<Page<Image>> getImageList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String category) {

        Page<Image> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Image> wrapper = new LambdaQueryWrapper<>();

        if (category != null && !category.isEmpty()) {
            wrapper.eq(Image::getCategory, category);
        }
        wrapper.orderByDesc(Image::getUploadTime);

        Page<Image> imagePage = imageService.page(pageParam, wrapper);
        return Result.success(imagePage);
    }

    /**
     * 获取影像详情
     */
    @GetMapping("/{id}")
    public Result<Image> getImageDetail(@PathVariable Long id) {
        Image image = imageService.getById(id);
        if (image != null) {
            image.setViewCount(image.getViewCount() + 1);
            imageService.updateById(image);
            return Result.success(image);
        }
        return Result.error("影像不存在");
    }

    /**
     * 删除影像
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteImage(@PathVariable Long id) {
        boolean success = imageService.removeById(id);
        if (success) {
            return Result.success("删除成功");
        }
        return Result.error("删除失败");
    }

    /**
     * 更新影像标签
     */
    @PutMapping("/tags")
    public Result<String> updateTags(@RequestBody TagDTO tagDTO) {
        Image image = imageService.getById(tagDTO.getImageId());
        if (image != null) {
            String tagsStr = String.join(",", tagDTO.getTags());
            image.setTags(tagsStr);
            imageService.updateById(image);
            return Result.success("标签更新成功");
        }
        return Result.error("影像不存在");
    }

    /**
     * 按标签搜索影像
     */
    @GetMapping("/search")
    public Result<Page<Image>> searchByTag(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        Page<Image> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Image> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(Image::getTags, keyword)
                .or()
                .like(Image::getName, keyword)
                .orderByDesc(Image::getUploadTime);

        return Result.success(imageService.page(pageParam, wrapper));
    }

    /**
     * 获取热门标签
     */
    @GetMapping("/hot-tags")
    public Result<List<Map<String, Object>>> getHotTags() {
        List<Image> images = imageService.list();
        Map<String, Integer> tagCount = new HashMap<>();

        for (Image image : images) {
            if (image.getTags() != null && !image.getTags().isEmpty()) {
                String[] tags = image.getTags().split(",");
                for (String tag : tags) {
                    String trimmedTag = tag.trim();
                    if (!trimmedTag.isEmpty()) {
                        tagCount.put(trimmedTag, tagCount.getOrDefault(trimmedTag, 0) + 1);
                    }
                }
            }
        }

        List<Map<String, Object>> result = new java.util.ArrayList<>();
        for (Map.Entry<String, Integer> entry : tagCount.entrySet()) {
            Map<String, Object> item = new HashMap<>();
            item.put("name", entry.getKey());
            item.put("count", entry.getValue());
            result.add(item);
        }

        result.sort((a, b) -> Integer.compare((int) b.get("count"), (int) a.get("count")));

        if (result.size() > 10) {
            result = result.subList(0, 10);
        }

        return Result.success(result);
    }

    /**
     * 点赞影像
     */
    @PostMapping("/like/{id}")
    public Result<String> likeImage(@PathVariable Long id) {
        Image image = imageService.getById(id);
        if (image != null) {
            image.setLikeCount(image.getLikeCount() == null ? 1 : image.getLikeCount() + 1);
            imageService.updateById(image);
            return Result.success("点赞成功");
        }
        return Result.error("影像不存在");
    }

    /**
     * 按分类统计数量
     */
    @GetMapping("/statistics")
    public Result<Map<String, Long>> getStatistics() {
        List<Image> images = imageService.list();
        Map<String, Long> statistics = new HashMap<>();

        for (Image image : images) {
            String category = image.getCategory();
            if (category != null) {
                statistics.put(category, statistics.getOrDefault(category, 0L) + 1);
            }
        }

        return Result.success(statistics);
    }
}