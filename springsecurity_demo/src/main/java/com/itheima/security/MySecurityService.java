package com.itheima.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 认证和授权自定义类
 *
 * InternalAuthenticationServiceException：认证服务异常 用户对不存在
 * BadCredentialsException：密码错误异常
 */
public class MySecurityService implements UserDetailsService{

    //bCryptPasswordEncoder.encode:密码加密方法
    //bCryptPasswordEncoder.matches():密码验证方法 参数1：用户页面输入的密码  参数2:数据查询的密码
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 根据用户名查询用户信息
     * @param username 用户名
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户名查询用户对象（伪代码）
        com.itheima.pojo.User user = this.getUser(username);
        //2.得到用户对象判断是否为空
        //为空return null
        if(user == null){
            return null;
        }
        //不为空获取数据库中的密码
        String password = user.getPassword();
        //授权ROLE_ADMIN add delete(后续从数据库中查询)
        List<GrantedAuthority> authorityList = new ArrayList<>();
        authorityList.add(new SimpleGrantedAuthority("add"));//单个权限
        authorityList.add(new SimpleGrantedAuthority("delete"));
        authorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));//角色权限
        //将密码放入User对象中（框架中user对象）
        //String username:用户输入的用户名, String password：数据库查询的,  Collection<? extends GrantedAuthority> authorities ：权限
        //return new User(username,"{noop}"+password,authorityList);
        return new User(username,password,authorityList);
    }

    /**
     * 模拟数据查询
     * @param username
     * @return
     */
    public com.itheima.pojo.User getUser(String username){
        if(username.equals("admin")){
            com.itheima.pojo.User user = new com.itheima.pojo.User();
            user.setUsername(username);
            user.setPassword(bCryptPasswordEncoder.encode("123456"));
            return user;
        }
        return null;
    }
}
