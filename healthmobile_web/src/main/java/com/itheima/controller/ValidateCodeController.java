package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

/**
 * 验证码的控制层
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 体检预约的验证码
     */
        @RequestMapping(value = "/send4Order",method = RequestMethod.POST)
        public Result send4Order(String telephone){

        try {
            //1.手机号（页面获取）、验证码（后台生成）发送验证码
            Integer code = ValidateCodeUtils.generateValidateCode(4);
            //调用短信接口发送验证码 您的验证码为:${code},该验证码5分钟内有效，请勿泄露于他人。
            System.out.println("手机号码：：：："+telephone+":::::验证码：：："+code);
            if (false) {
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code + "");
            }
            //2.将验证码存入redis后续校验
            //key:001_15111111111   seconds:  value:
            jedisPool.getResource().setex(RedisMessageConstant.SENDTYPE_ORDER+"_"+telephone,5*60,code.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }


    /**
     * 体检预约的验证码
     */
    @RequestMapping(value = "/send4Login",method = RequestMethod.POST)
    public Result send4Login(String telephone){
        //1.发送快速登录的验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        //2.调用短信接口
        try {
            System.out.println("手机号码：：：："+telephone+"验证码：：：："+code);
            if(false) {
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code.toString());
            }
        } catch (ClientException e) {
            e.printStackTrace();
        }
        //3.将验证码存入redis
        jedisPool.getResource().setex(RedisMessageConstant.SENDTYPE_LOGIN+"_"+telephone,5*60,code.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
