package com.campus.demo.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("category")
public class Category {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;        // 分类名称：activity/course/scenery/other
    private String label;       // 显示名称：校园活动/课程记录/校园风景/其他
    private String icon;        // 图标
    private Integer sortOrder;  // 排序
    private Integer status;     // 状态：0禁用，1启用
    private LocalDateTime createTime;
    @TableLogic
    private Integer deleted;
}