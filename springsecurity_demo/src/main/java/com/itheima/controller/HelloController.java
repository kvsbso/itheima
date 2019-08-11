package com.itheima.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/hello")
public class HelloController {

    /**
     * @PreAuthorize:权限注解
     * 访问：/hello/add.do 需要add权限
     * 在方法上设置权限关键字 不能随便定义，一定要跟数据库中role或permission表中keyword保持一致
     * @return
     */
    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('add')")//表示用户必须拥有add权限才能调用当前方法
    public String add(){
        System.out.println("add...");
        return null;
    }


    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('sssss')")//表示用户必须拥有ROLE_ADMIN角色才能调用当前方法
    public String delete(){
        System.out.println("delete...");
        return null;
    }
}