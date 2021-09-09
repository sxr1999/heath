package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;

import java.util.List;

/**
 * @program: itcast_health
 * @description
 * @author: ShenXinran
 * @create: 2021-08-10 16:11
 **/
public interface CheckItemService {

    public void add(CheckItem checkItem);
    public PageResult pageQuery(QueryPageBean queryPageBean);

    public void deleteById(Integer id);

    void edit(CheckItem checkItem);

    public CheckItem findById(Integer id);


    List<CheckItem> findAll();
}
