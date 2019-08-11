package com.itheima.dao;

import com.itheima.pojo.User;

/**
 * 用户接口
 */
public interface UserDao {
    /**
     * 根据用户名查询用户对象
     * @param username
     * @return
     */
    User findUserByUsername(String username);
}
