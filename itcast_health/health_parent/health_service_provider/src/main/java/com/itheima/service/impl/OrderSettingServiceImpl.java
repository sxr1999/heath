package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @program: itcast_health
 * @description
 * @author: ShenXinran
 * @create: 2021-08-13 20:46
 **/
@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
    @Autowired
    private OrderSettingDao orderSettingDao;

    //批量导入预约设置
    public void add(List<OrderSetting> list) {
        if (list != null && list.size()>0){
            for (OrderSetting orderSetting : list) {
                //判断当前日期是否已经有数据
                long countByOrderDate = orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
                if (countByOrderDate>0){
                    //当前日期内已经有数据，需要执行更新操作
                    orderSettingDao.editNumberByOrderDate(orderSetting);
                }else {
                    //当前日期内没有值，执行插入操作
                    orderSettingDao.add(orderSetting);
                }
            }
        }
    }

    public List<Map> getOrderSettingByMonth(String date) {
        String[] split = date.split("-");
        String year = split[0];
        String month = split[1];
        /*String begin = date + "-1";
        String end = date + "-31";*/
        Map<String,String> map = new HashMap<String, String>();
        map.put("year",year);
        map.put("month",month);
        List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
        List<Map> result = new ArrayList<Map>();
        if (list != null && list.size() >0){
            for (OrderSetting orderSetting : list) {
                Map<String,Object> m = new HashMap<String, Object>();
                m.put("date",orderSetting.getOrderDate().getDate());
                m.put("number",orderSetting.getNumber());
                m.put("reservations",orderSetting.getReservations());
                result.add(m);
            }
        }
        return result;
    }

    public void editNumberByDate(OrderSetting orderSetting) {
        //判断当前日期是否已经有预约
        Date orderDate = orderSetting.getOrderDate();
        long count = orderSettingDao.findCountByOrderDate(orderDate);
        if (count>0){
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else {
            orderSettingDao.add(orderSetting);
        }
    }
}
