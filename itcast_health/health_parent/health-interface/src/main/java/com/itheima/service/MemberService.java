package com.itheima.service;

import com.alibaba.dubbo.rpc.listener.ListenerInvokerWrapper;
import com.itheima.pojo.Member;

import java.util.List;

/**
 * @program: itcast_health
 * @description
 * @author: ShenXinran
 * @create: 2021-08-20 11:46
 **/
public interface MemberService {
    //根据手机号查会员
    public Member findByTelephone(String telephone);
    //注册会员
    public void add(Member member);
    //根据月份查询数据
    public List<Integer> findMemberCountByMonths(List<String> months);
}
