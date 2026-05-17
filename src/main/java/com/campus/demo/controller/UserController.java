package com.campus.demo.controller;

import com.campus.demo.common.Result;
import com.campus.demo.entity.User;
import com.campus.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@CrossOrigin  // 允许前端跨域访问
public class UserController {

    @Autowired
    private UserService userService;

    // 登录接口
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginInfo) {
        String username = loginInfo.get("username");
        String password = loginInfo.get("password");

        User user = userService.login(username, password);

        if (user != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("id", user.getId());
            data.put("username", user.getUsername());
            data.put("realName", user.getRealName());
            data.put("role", user.getRole());
            return Result.success(data);
        } else {
            return Result.error("用户名或密码错误");
        }
    }

    // 获取用户信息
    @GetMapping("/info/{id}")
    public Result<User> getUserInfo(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user != null) {
            user.setPassword(null); // 不返回密码
            return Result.success(user);
        }
        return Result.error("用户不存在");
    }
}