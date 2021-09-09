package com.itheima.dao;

import com.itheima.pojo.Role;

import java.util.Set;

/**
 * @program: itcast_health
 * @description
 * @author: ShenXinran
 * @create: 2021-08-21 12:14
 **/
public interface RoleDao {
    public Set<Role> findByUserId(Integer UserId);
}
