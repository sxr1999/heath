package com.itheima.service;

import com.itheima.entity.Result;

import java.util.Map;

/**
 * @program: itcast_health
 * @description
 * @author: ShenXinran
 * @create: 2021-08-18 17:20
 **/
public interface OrderService {
    public Result order(Map map) throws Exception;

    Map findById(Integer id) throws Exception;
}
