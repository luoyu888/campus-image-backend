package com.campus.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.campus.demo.common.Result;
import com.campus.demo.entity.Image;
import com.campus.demo.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/image")
@CrossOrigin  // 允许前端跨域访问
public class ImageController {

    @Autowired
    private ImageService imageService;

    /**
     * 上传影像
     * POST /api/image/upload
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
     * GET /api/image/list?page=1&size=10&category=activity
     */
    @GetMapping("/list")
    public Result<Page<Image>> getImageList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String category) {

        Page<Image> pageParam = new Page<>(page, size);
        LambdaQueryWrapper<Image> wrapper = new LambdaQueryWrapper<>();

        // 按分类筛选
        if (category != null && !category.isEmpty()) {
            wrapper.eq(Image::getCategory, category);
        }

        // 按上传时间倒序
        wrapper.orderByDesc(Image::getUploadTime);

        Page<Image> imagePage = imageService.page(pageParam, wrapper);
        return Result.success(imagePage);
    }

    /**
     * 获取影像详情
     * GET /api/image/{id}
     */
    @GetMapping("/{id}")
    public Result<Image> getImageDetail(@PathVariable Long id) {
        Image image = imageService.getById(id);
        if (image != null) {
            // 增加浏览次数
            image.setViewCount(image.getViewCount() + 1);
            imageService.updateById(image);
            return Result.success(image);
        }
        return Result.error("影像不存在");
    }

    /**
     * 删除影像
     * DELETE /api/image/{id}
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
     * 按分类获取影像数量统计
     * GET /api/image/statistics
     */
    @GetMapping("/statistics")
    public Result<Object> getStatistics() {
        LambdaQueryWrapper<Image> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Image::getCategory, Image::getViewCount);
        return Result.success(imageService.list(wrapper));
    }
}