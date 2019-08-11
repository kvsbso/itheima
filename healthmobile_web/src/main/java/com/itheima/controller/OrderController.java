package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * 预约体检-控制层
 */
@RestController
@RequestMapping("/order")
public class OrderController {

    @Reference
    private OrderService orderService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 预约体检
     *
     * @return
     */
    @RequestMapping(value = "/submit", method = RequestMethod.POST)
    public Result submitOrder(@RequestBody Map map) {
        //获取map中的数据
        //1.获取数据
        String telephone = (String) map.get("telephone");
        String orderDate = (String) map.get("orderDate");
        String mapCode = (String) map.get("validateCode");//页面的验证码
        String redisCode = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone);
        Result result = null;
        //redis中的验证码
        try {
            if (!StringUtils.isEmpty(redisCode) && !StringUtils.isEmpty(mapCode) && redisCode.equals(mapCode)) {
                //2.调用服务进行业务处理
                //将Order放入result中data属性中
                map.put("orderType","微信预约");//预约类型 微信预约 电话预约
                result = orderService.submitOrder(map);
                //3.发送短信通知
                if (result.isFlag()) {
                    try {
                        if(false){
                            SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE, telephone, orderDate);
                        }
                    } catch (ClientException e) {
                        e.printStackTrace();
                    }
                    System.out.println("体检预约成功了。。。。");
                    return new Result(true, MessageConstant.ORDER_SUCCESS, result.getData());//data最终要返回一些数据
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("体检预约失败了。。。。");
        String message = MessageConstant.ORDER_FAIL;
        if(!StringUtils.isEmpty(result.getMessage())){
            message= result.getMessage();
        }
        return new Result(false,message );//data最终要返回一些数据
    }

    /**
     * 预约成功详情页
     */
    @RequestMapping(value = "/findById",method = RequestMethod.POST)
    public Result findById(Integer id){
        Map map = orderService.findById(id);
        return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);//data最终要返回一些数据
    }
}
