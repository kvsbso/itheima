package com.itheima.security;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class SpringSecurityUserService implements UserDetailsService {


    @Reference
    private UserService userService;



    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.调用业务, 根据用户名查询
        User user  =  userService.findUserByUsername(username);
        if(user == null){
            return null;
        }

        //2.进行授权
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();

        Set<Role> roles = user.getRoles();
        for (Role role : roles) {
            Set<Permission> permissions = role.getPermissions();
            for (Permission permission : permissions) {
                grantedAuthorityList.add(new SimpleGrantedAuthority(permission.getKeyword()));
            }
        }


        //3.创建UserDetails对象返回
        org.springframework.security.core.userdetails.User userDetail = new org.springframework.security.core.userdetails.User(username, user.getPassword(), grantedAuthorityList);
        return userDetail;
    }
}
