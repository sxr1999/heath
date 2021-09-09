package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @program: itcast_health
 * @description
 * @author: ShenXinran
 * @create: 2021-08-18 11:07
 **/
//验证码操作
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {
    @Autowired
    private JedisPool jedisPool;

    //用户体检预约发送验证码
    @RequestMapping("/send40rder")
    public Result send4Order(String telephone){
        //随机生成验证码
        Integer code = ValidateCodeUtils.generateValidateCode(4);
        //给用户发送验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //保存用户吗到redis中 手机号+001作为key
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,300,code.toString());

        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    //用户手机快速登录发送验证码
    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        //随机生成验证码
        Integer code = ValidateCodeUtils.generateValidateCode(6);
        //给用户发送验证码
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,code.toString());
        } catch (ClientException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        //保存用户吗到redis中 手机号+001作为key
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_LOGIN,300,code.toString());

        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }
}
