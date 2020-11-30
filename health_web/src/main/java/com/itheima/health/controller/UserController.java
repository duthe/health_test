package com.itheima.health.controller;

import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/findUserName")
    public Result findUserName() {
        // 获取登陆用户的认证信息
        User userDetails = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 登陆用户名

        String username = userDetails.getUsername();

        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,username);
    }

}
