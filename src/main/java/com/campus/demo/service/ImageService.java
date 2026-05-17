package com.campus.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.demo.entity.Image;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService extends IService<Image> {

    /**
     * 上传影像
     * @param file 上传的文件
     * @param category 分类
     * @param uploaderId 上传者ID
     * @return 文件访问路径
     */
    String uploadImage(MultipartFile file, String category, Long uploaderId) throws Exception;
}