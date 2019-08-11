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

/**
 * 自定义认证和授权类
 */
@Component
public class SecurityUserService  implements UserDetailsService{

    //注入服务
    @Reference
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1.根据用户名查询用户对象
        User user = userService.findUserByUsername(username);
        //2.判断用户对象是否存在 不存在retun null
        if(user == null){
            return null;//账号不存在
        }
        String password = user.getPassword();//数据库中查询的密码 已经加密
        //3.用户存在 根据用户获取权限数据 授权
        Set<Role> roles = user.getRoles();//获取所有角色数据
        List<GrantedAuthority> grantedAuthorityList = new ArrayList<>();
        if(roles != null && roles.size()>0){
            for (Role role : roles) {
                String roleKeyWord = role.getKeyword();//角色权限关键字  将关键字授予当前登录的用户
                grantedAuthorityList.add(new SimpleGrantedAuthority(roleKeyWord));
                //获取权限关键字
                Set<Permission> permissions = role.getPermissions();
                if(permissions != null && permissions.size()>0){
                    for (Permission permission : permissions) {
                        String permissionKeyWord = permission.getKeyword();
                        grantedAuthorityList.add(new SimpleGrantedAuthority(permissionKeyWord));
                    }
                }
            }
        }
        //4.返回框架user对象
        return new org.springframework.security.core.userdetails.User(username,password,grantedAuthorityList);
    }
}
