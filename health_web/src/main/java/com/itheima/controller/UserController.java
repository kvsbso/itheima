package com.itheima.controller;

import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户控制层
 */
@RestController
@RequestMapping("/user")
public class UserController {

    /**
     * 获取用户名
     */
    @RequestMapping(value = "/getUsername",method = RequestMethod.POST)
    public Result getUsername(){
        //spring security框架上下文对象
        //getContext():权限容器 getAuthentication():获取认证对象     getPrincipal()获取用户信息
        User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
    }
}
