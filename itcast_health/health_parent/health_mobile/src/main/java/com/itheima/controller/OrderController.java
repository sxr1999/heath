package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

/**
 * @program: itcast_health
 * @description
 * @author: ShenXinran
 * @create: 2021-08-18 17:04
 **/
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private OrderService orderService;
    //在线体检预约
    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){
        //获取map中的手机号和验证码
        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        Object date = map.get("orderDate");
        //校对验证码
        String code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
        //用户输入的验证码和redis中保存的验证码经行比对
        if (validateCode!=null && code!=null && validateCode.equals(code)){
            //添加预约类型：电话预约、微信预约。。。
            map.put("orderType", Order.ORDERTYPE_WEIXIN);
            //比对成功调用service
            Result result = null;
            try {
                 result = orderService.order(map);
            }catch (Exception e){
                e.printStackTrace();
                return result;
            }

            if (result.isFlag()){
                //预约成功，发送短信
                try {
                    SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,date.toString());
                } catch (ClientException e) {
                    e.printStackTrace();
                }
            }
            return  result;
        }else {
            //比对不成功，返回错误提示信息给页面
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }


    //根据预约id查询预约相关信息
    @RequestMapping("/findById")
    public Result findById(Integer id){
        try{
            Map map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
