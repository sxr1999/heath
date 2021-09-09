package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: itcast_health
 * @description
 * @author: ShenXinran
 * @create: 2021-08-11 17:30
 **/
@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;
    //新增检查组,同时关联检查项
    public void add(CheckGroup checkGroup, Integer[] checkitemIds) {
        //新增检查组 t_checkgroup;
        checkGroupDao.add(checkGroup);
        //设置检查组和检查项的关联关系
        Integer checkGroupId = checkGroup.getId();

        this.setCheckGroupAndCheckItem(checkGroupId,checkitemIds);
    }

    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer pageSize = queryPageBean.getPageSize();
        Integer currentPage = queryPageBean.getCurrentPage();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> checkGroups = checkGroupDao.selectByCondition(queryString);
        return new PageResult(checkGroups.getTotal(),checkGroups.getResult());
    }

    public CheckGroup findById(Integer id) {
        return checkGroupDao.findById(id);
    }

    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    public void edit(CheckGroup checkGroup, Integer[] checkitemIds) {
        //修改检查组的基本信息
        checkGroupDao.edit(checkGroup);
        //清理当前检查组关联的检查项，操作中间关系表
        checkGroupDao.deleteAssocication(checkGroup.getId());
        //重新建立当前检查组和检查项的关联关系
        Integer checkGroupId = checkGroup.getId();
        this.setCheckGroupAndCheckItem(checkGroupId,checkitemIds);

    }

    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }








    public void setCheckGroupAndCheckItem(Integer checkGroupId,Integer[] checkitemIds){
        if (checkitemIds != null && checkitemIds.length>0){
            for (Integer checkitemId : checkitemIds) {
                Map<String,Integer> map =new HashMap<String,Integer>();
                map.put("checkgroupId",checkGroupId);
                map.put("checkitemId",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItem(map);
            }
        }
    }
}
