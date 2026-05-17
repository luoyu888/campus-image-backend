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
    private Long uploaderId;
    private LocalDateTime uploadTime;
    private Long viewCount;
    @TableLogic
    private Integer deleted;
}