package com.itheima.dao;

import com.itheima.pojo.User;

/**
 * @program: itcast_health
 * @description
 * @author: ShenXinran
 * @create: 2021-08-21 12:11
 **/
public interface UserDao {
    public User findByUsername(String username);
}
