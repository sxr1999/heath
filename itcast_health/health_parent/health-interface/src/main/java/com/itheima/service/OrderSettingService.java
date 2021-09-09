package com.itheima.service;

import com.itheima.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * @program: itcast_health
 * @description
 * @author: ShenXinran
 * @create: 2021-08-13 16:57
 **/
public interface OrderSettingService {
    public void add(List<OrderSetting> data);

    List<Map> getOrderSettingByMonth(String date);

    void editNumberByDate(OrderSetting orderSetting);

}
