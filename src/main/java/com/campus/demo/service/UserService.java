package com.campus.demo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.campus.demo.entity.User;

public interface UserService extends IService<User> {
    User login(String username, String password);
}