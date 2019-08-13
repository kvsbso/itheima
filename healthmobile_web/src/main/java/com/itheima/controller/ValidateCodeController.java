package com.itheima.controller;


import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    //体检预约时发送手机验证码
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone) {
        //调用工具类生成验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        try {
            //调用短信业务发送验证码
//            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code.toString());

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.QUERY_SETMEAL_SUCCESS);
        }
        //将验证码存储到redis中
        System.out.println("发送的手机验证码为：" + code);
        jedisPool.getResource().setex(RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone, 50 * 60, code.toString());

        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS);
    }

    @RequestMapping("/send4Login")
    public Result send4Login(String telephone) {
        //调用工具类生成验证码
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        //调用短信发送验证码
        try {
            if (false) {
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code.toString());
            }
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //将验证码存储到redis中
        jedisPool.getResource().setex(RedisMessageConstant.SENDTYPE_LOGIN+"_"+telephone,60*5 ,code.toString());
        return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

}
