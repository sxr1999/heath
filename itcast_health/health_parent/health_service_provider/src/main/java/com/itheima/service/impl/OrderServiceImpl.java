package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import com.sun.codemodel.internal.JVar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @program: itcast_health
 * @description 体检预约服务
 * @author: ShenXinran
 * @create: 2021-08-18 20:29
 **/
@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;
    public Result order(Map map) throws Exception{
        //1.检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法预约
        String orderDate = (String) map.get("orderDate");//预约日期
        Date date = DateUtils.parseString2Date(orderDate);
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);
        if (orderSetting==null){
            //指定日期没有进行预约设置，无法完成预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //2.检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
        int number = orderSetting.getNumber();//可预约人数
        int reservations = orderSetting.getReservations();//已预约人数
        if (reservations>=number){
            //已经约满,无法预约
            return new Result(false,MessageConstant.ORDER_FULL);
        }

        //3.检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
        String telephone = (String) map.get("telephone");//获取手机号
        Member member = memberDao.findByTelephone(telephone);
        if (member != null){
            //判断是否重复预约
            Integer memberId = member.getId();
            Date date1 = DateUtils.parseString2Date(orderDate);
            String setmealId = (String) map.get("setmealId");
            Order order = new Order(memberId,date1,Integer.parseInt(setmealId));
            //根据条件查询
            List<Order> list = orderDao.findByCondition(order);
            if (list != null && list.size()>0){
                //已有数据，用户在重复查询,无法再次预约
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }else {
            //4.检查用户是否是会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
            member= new Member();
            member.setPhoneNumber(telephone);
            member.setName((String) map.get("name"));
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            member.setRegTime(new Date());
            memberDao.add(member);//完成会员注册
        }

        //5.预约成功，更新当日的已预约人数
        Order order = new Order();
        order.setMemberId(member.getId());
        order.setOrderDate(DateUtils.parseString2Date(orderDate));
        order.setOrderType((String) map.get("orderType"));
        order.setOrderStatus(Order.ORDERSTATUS_NO);//到诊状态
        order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));//设置套餐id
        orderDao.add(order);

        orderSetting.setReservations(orderSetting.getReservations()+1);

        orderSettingDao.editReservationsByOrderDate(orderSetting);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    //根据预约id查询预约相关信息（体检人姓名，预约日期，套餐名称，预约类型）
    public Map findById(Integer id) throws Exception{
        Map map = orderDao.findById4Detail(id);
        if (map!=null){
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}
