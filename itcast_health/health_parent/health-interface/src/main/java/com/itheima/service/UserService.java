package com.itheima.service;

import com.itheima.pojo.User;

/**
 * @program: itcast_health
 * @description
 * @author: ShenXinran
 * @create: 2021-08-21 11:55
 **/
public interface UserService {
    public User findByUsername(String username);
}
