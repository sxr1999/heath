package com.itheima.test;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.utils.SMSUtils;

/**
 * @program: itcast_health
 * @description
 * @author: ShenXinran
 * @create: 2021-08-17 16:34
 **/
public class AliTest {
    public static void main(String[] args) throws ClientException {
        SMSUtils.sendShortMessage("SMS_192542265","18006176200","1234");
    }
}
