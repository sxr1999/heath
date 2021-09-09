package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckItemDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @program: itcast_health
 * @description
 * @author: ShenXinran
 * @create: 2021-08-10 16:23
 **/
@Service(interfaceClass = CheckItemService.class)
@Transactional
public class CheckItemControllerImpl implements CheckItemService {
    @Autowired
    private CheckItemDao checkItemDao;

    public void add(CheckItem checkItem) {
        checkItemDao.add(checkItem);
    }

    //检查项分页查询
    public PageResult pageQuery(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckItem> page = checkItemDao.selectByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    //删除检查项
    public void deleteById(Integer id) {
        //判断当前检查项是否已经关联检查组
        long count = checkItemDao.findCountByCheckItemId(id);
        if (count>0){
            //当前项目已经被关联到检查组，不允许删除
            new RuntimeException();
        }
        checkItemDao.deleteById(id);
    }

    public void edit(CheckItem checkItem) {
        checkItemDao.edit(checkItem);
    }

    public CheckItem findById(Integer id) {
        return checkItemDao.findById(id);
    }

    public List<CheckItem> findAll() {
        return checkItemDao.findAll();
    }


}
