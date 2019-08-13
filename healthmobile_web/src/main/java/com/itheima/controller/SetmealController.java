package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * 套餐控制层管理
 */
@Controller
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/getSetmeal")
    public Result getSetmeal() {
        try {
            //先从redis数据库查询数据
            String setmealList1 = jedisPool.getResource().get("setmealList");
           // List<Setmeal> parse = (List<Setmeal>) JSON.parse(setmealList1);
            //如果redis中没有数据
            if (setmealList1 == null) {
                //调用数据库查询
                List<Setmeal> setmeal = setmealService.getSetmeal();
                //将java对象转成json数据格式
                String jsonString = JSON.toJSONString(setmeal);
                //将json数据格式存入到redis中
                jedisPool.getResource().set("setmealList",jsonString);
                return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
            }else {
                //如果redis中有数据,就直接返回数据
                return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmealList1);
            }



        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

    @RequestMapping("/findById")
    public @ResponseBody Result findById(Integer id) {
        try {
            Setmeal setmeal = setmealService.findById(id);
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, setmeal);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
        }
    }

}
