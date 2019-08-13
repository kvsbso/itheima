package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.PermissionDao;
import com.itheima.dao.RoleDao;
import com.itheima.dao.UserDao;
import com.itheima.pojo.Permission;
import com.itheima.pojo.Role;
import com.itheima.pojo.User;
import com.itheima.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;


@Service(interfaceClass = UserService.class)
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private PermissionDao permissionDao;


    /**
     * 根据用户名查询User
     * @param username
     * @return
     */
    @Override
    public User findUserByUsername(String username) {
        //根据用户名查询User
        User user =  userDao.findUserByUsername(username);

        if(user != null){
            //根据用户id查询角色
            Set<Role> roles =  roleDao.findByUid(user.getId());

            //根据角色id查询角色
            if(roles != null && roles.size() > 0){
                for (Role role : roles) {
                    Integer roleId = role.getId();
                    Set<Permission> permissions =  permissionDao.findByRoleId(roleId);
                    role.setPermissions(permissions);
                }

                user.setRoles(roles);
            }
        }

        return user;
    }
}
