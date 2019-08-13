package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Menu;
import com.itheima.service.SetmealService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Reference
    private SetmealService setmealService;


    //获取当前登录用户的用户名
    @RequestMapping("/getUsername")
    public Result getUsername()throws Exception{
        //通过spring security框架中提供的SecurityContextHolder来获取用户对象
        //getContext()获取安全容器对象
        //getAuthentication()获取认证
        //getPrincipal()最终获取用户对象
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
        return new Result(true,MessageConstant.GET_MENU_SUCCESS,username);
    }

        //"path": "1",
        //"title": "工作台",
        //"icon":"fa-dashboard",
        //"children": []
    @RequestMapping("/permissions")
    public Result permission(){
        User principal = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = principal.getUsername();
        try {
            Map<String,Object> map = new HashMap<>();
                    //根据用户名查询权限表
            Integer roleId = setmealService.queryJurisdictionByUser(username);
            //根据权限id查询显示列表
            List<Map> list = setmealService.JurisdictionById(roleId);


            return new Result(true,MessageConstant.GET_MENU_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_MENU_SUCCESS);
        }
    }
}
