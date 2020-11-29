package com.itheima.health.service;

import com.itheima.health.pojo.User;

public interface UserService {

    /**
     * 根据用户名查询用户 包括  角色 权限
     * @param username
     * @return
     */
    User findByUsername(String username);
}
