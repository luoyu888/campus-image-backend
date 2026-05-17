package com.campus.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.demo.entity.Category;
import java.util.List;

public interface CategoryService extends IService<Category> {
    List<Category> getAllEnabled();
}