package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;
/**
 * 快速登录
 */
@RestController
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;


    /**
     * 快速登录
     */
    @RequestMapping(value = "/check",method = RequestMethod.POST)
    public Result login(HttpServletResponse response,@RequestBody Map map){
        //后台接收校验验证码，，存在跳转成功页面，不存在自动注册会员，将用户的信息存入cookie中。
        //telephone validateCode
        String telephone = (String)map.get("telephone");
        //获取用户输入的验证码
        String validateCode = (String)map.get("validateCode");
        ///获取redis中验证码
        String redisCode = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone);
        //校验验证码、
        if(StringUtils.isEmpty(validateCode) || StringUtils.isEmpty(redisCode) || !validateCode.equals(redisCode)){
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
        //根据手机号查询会员表是否存在
        Member member = memberService.findByTelephone(telephone);
        //判断会员是否存在
        if(member == null){
            //自动注册
            member = new Member();
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            memberService.add(member);
        }
        //用户对象存入cookie
        Cookie cookie = new Cookie("login_member_telephone",telephone);
        cookie.setPath("/");//路径
        cookie.setMaxAge(60*60*24*30);//1一个月有效
        response.addCookie(cookie);
        return new Result(true, MessageConstant.LOGIN_SUCCESS);
    }
}
