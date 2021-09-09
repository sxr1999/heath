package com.itheima.dao;

import com.itheima.pojo.Permission;

import java.util.Set;

/**
 * @program: itcast_health
 * @description
 * @author: ShenXinran
 * @create: 2021-08-21 12:18
 **/
public interface PermissionDao {
    public Set<Permission> findByRoleId(Integer roleId);
}
