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

    /**
     * 获取所有分类列表
     */
    @GetMapping("/list")
    public Result<List<Category>> getCategoryList() {
        return Result.success(categoryService.getAllEnabled());
    }

    /**
     * 获取所有分类（包括禁用的）
     */
    @GetMapping("/all")
    public Result<List<Category>> getAllCategories() {
        return Result.success(categoryService.list());
    }

    /**
     * 添加分类
     */
    @PostMapping("/add")
    public Result<String> addCategory(@RequestBody Category category) {
        categoryService.save(category);
        return Result.success("添加成功");
    }

    /**
     * 更新分类
     */
    @PutMapping("/update")
    public Result<String> updateCategory(@RequestBody Category category) {
        categoryService.updateById(category);
        return Result.success("更新成功");
    }

    /**
     * 删除分类
     */
    @DeleteMapping("/{id}")
    public Result<String> deleteCategory(@PathVariable Long id) {
        categoryService.removeById(id);
        return Result.success("删除成功");
    }

    /**
     * 启用/禁用分类
     */
    @PutMapping("/status/{id}")
    public Result<String> updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        Category category = categoryService.getById(id);
        if (category != null) {
            category.setStatus(status);
            categoryService.updateById(category);
            return Result.success(status == 1 ? "已启用" : "已禁用");
        }
        return Result.error("分类不存在");
    }
}