package com.campus.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.campus.demo.entity.Image;
import com.campus.demo.mapper.ImageMapper;
import com.campus.demo.service.ImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class ImageServiceImpl extends ServiceImpl<ImageMapper, Image> implements ImageService {

    @Value("${file.upload.path}")
    private String uploadPath;

    @Override
    public String uploadImage(MultipartFile file, String category, Long uploaderId) throws Exception {
        // 1. 获取原始文件名和后缀
        String originalName = file.getOriginalFilename();
        String ext = originalName.substring(originalName.lastIndexOf("."));

        // 2. 生成新文件名（UUID防止重名）
        String newFileName = UUID.randomUUID().toString() + ext;

        // 3. 创建目录（按分类存放）
        File categoryDir = new File(uploadPath + category);
        if (!categoryDir.exists()) {
            categoryDir.mkdirs();
        }

        // 4. 保存文件到服务器
        File destFile = new File(categoryDir, newFileName);
        file.transferTo(destFile);

        // 5. 保存记录到数据库
        Image image = new Image();
        image.setName(originalName);
        image.setUrl("/uploads/" + category + "/" + newFileName);
        image.setCategory(category);
        image.setUploaderId(uploaderId);
        image.setUploadTime(LocalDateTime.now());
        image.setViewCount(0L);

        this.save(image);

        // 6. 返回文件访问路径
        return image.getUrl();
    }
}