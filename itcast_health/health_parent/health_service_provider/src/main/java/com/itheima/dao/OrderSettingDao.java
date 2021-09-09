package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: itcast_health
 * @description
 * @author: ShenXinran
 * @create: 2021-08-13 20:49
 **/
public interface OrderSettingDao {
    public void add(OrderSetting orderSetting);

    public void editNumberByOrderDate(OrderSetting orderSetting);

    public long findCountByOrderDate(Date orderDate);

    List<OrderSetting> getOrderSettingByMonth(Map<String, String> map);

    OrderSetting findByOrderDate(Date date);

    public void editReservationsByOrderDate(OrderSetting orderSetting);

}
