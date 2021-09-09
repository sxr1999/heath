package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

/**
 * @program: itcast_health
 * @description
 * @author: ShenXinran
 * @create: 2021-08-20 11:37
 **/
@RequestMapping("/member")
@RestController
public class MemberController {
    @Autowired
    private JedisPool jedisPool;
    @Reference
    private MemberService memberService;
    //手机号快速登录
    @RequestMapping("/login")
    public Result login(HttpServletResponse response, @RequestBody Map map){
        //获取手机号
        String telephone = (String) map.get("telephone");
        //获取页面的验证码
        String validateCode = (String) map.get("validateCode");
        String codeInRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);
        if (validateCode != null && codeInRedis!=null && validateCode.equals(codeInRedis)){
            //验证码输入正确
            //查询是否是会员
            Member member = memberService.findByTelephone(telephone);
            if (member==null){
                //不是会员，自动注册会员
                member = new Member();
                member.setRegTime(new Date());
                member.setPhoneNumber(telephone);
                memberService.add(member);
            }
            //向浏览器写入cookie，内容为手机号
            Cookie cookie = new Cookie("login_member_telephone",telephone);
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24*30);
            response.addCookie(cookie);
            //将会员信息保存的redis
            String json = JSON.toJSON(member).toString();//将会员信息序列化为json串
            jedisPool.getResource().setex(telephone,60*30,json);
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        }else {
            //验证码错误
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
    }

}
