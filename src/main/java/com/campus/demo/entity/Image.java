package com.campus.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("image")
public class Image {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String url;
    private String thumbnailUrl;
    private String category;
    private String tags;          // 添加这个
    private String description;   // 添加这个
    private Long uploaderId;
    private LocalDateTime uploadTime;
    private Long viewCount;
    private Long likeCount;       // 添加这个
    private Long downloadCount;   // 添加这个
    @TableLogic
    private Integer deleted;
}