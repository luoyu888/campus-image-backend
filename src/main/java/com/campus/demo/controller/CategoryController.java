package com.campus.demo.controller;

import com.campus.demo.common.Result;
import com.campus.demo.entity.Category;
import com.campus.demo.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/category")
@CrossOrigin
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/list")
    public Result<List<Category>> getCategoryList() {
        return Result.success(categoryService.getAllEnabled());
    }

    @PostMapping("/add")
    public Result<String> addCategory(@RequestBody Category category) {
        categoryService.save(category);
        return Result.success("添加成功");
    }

    @PutMapping("/update")
    public Result<String> updateCategory(@RequestBody Category category) {
        categoryService.updateById(category);
        return Result.success("更新成功");
    }

    @DeleteMapping("/{id}")
    public Result<String> deleteCategory(@PathVariable Long id) {
        categoryService.removeById(id);
        return Result.success("删除成功");
    }
}