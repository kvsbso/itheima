package com.itheima.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;


    @Autowired
    private JedisPool jedisPool;


    @RequestMapping("/submit")
    public Result submitOrder(@RequestBody Map map) {

        //获取map参数
        //获取页面输入的电话
        String telephone = (String) map.get("telephone");
        //获取页面输入的时间
        String orderDate = (String) map.get("orderDate");
        String validateCode = (String) map.get("validateCode");//页面输入的验证码
        //获取存在redis中的验证码
        String redisCode = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone);

        Result result = null;
        //非空判断并且比较是否相等
        if (StringUtils.isEmpty(validateCode) && StringUtils.isEmpty(redisCode) && redisCode.equals(validateCode)){

            try {
                result = orderService.submitOrder(map);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (result.isFlag()) {
                try {
                    SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone, orderDate);
                } catch (ClientException e) {
                    e.printStackTrace();
                }
            }

        }
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, result.getData());

    }
}
