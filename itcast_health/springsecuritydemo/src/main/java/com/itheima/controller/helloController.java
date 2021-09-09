package com.itheima.controller;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @program: itcast_health
 * @description
 * @author: ShenXinran
 * @create: 2021-08-20 22:54
 **/
@RestController
@RequestMapping("/hello")
public class helloController {

    @RequestMapping("/add")
    @PreAuthorize("hasAuthority('add')")
    public String add(){
        System.out.println("add......");
        return "success";
    }

    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public String delete(){
        System.out.println("delete.....");
        return "delete";
    }
}
