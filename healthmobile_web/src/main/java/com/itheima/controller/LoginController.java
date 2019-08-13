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
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class LoginController {

    @Reference
    private MemberService memberService;

    @Autowired
    private JedisPool jedisPool;

    @RequestMapping("/check")
    public Result check(HttpServletResponse response, @RequestBody Map map){
        //获取页面传过来的手机号和验证码
        String telephone = (String)map.get("telephone");
        String validateCode = (String)map.get("validateCode");
        //从Redis中获取缓存的验证码
        String code = jedisPool.getResource().get(RedisMessageConstant.SENDTYPE_LOGIN);
        if (StringUtils.isEmpty(code) || StringUtils.isEmpty(validateCode) || !code.equals(validateCode)){
            //验证码输入错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }else {
            //判断当前用户是否为会员
            Member member = memberService.findByTelephone(telephone);
            //如果不是会员,则自动注册
            if (member == null){
                Member member1 = new Member();
                member1.setPhoneNumber(telephone);
                member1.setRegTime(new Date());
                memberService.add(member1);
            }
            //登录成功,将用户名和密码存入cookie
            Cookie cookie = new Cookie("login_member_telephone",telephone );
            cookie.setMaxAge(60*60*24*30);
            cookie.setPath("/");
            response.addCookie(cookie);
            return new Result(true, MessageConstant.LOGIN_SUCCESS);
        }
    }
}
