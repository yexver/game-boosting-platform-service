package com.jmz.serverorder.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @GetMapping("/test01")
    //@PreAuthorize("hasRole('ROLE_admin')")
    @PreAuthorize("hasAuthority('permission:manage')")
    public String test() {
        //输出当前用户信息
        System.out.println("用户信息："+ SecurityContextHolder.getContext().getAuthentication().toString());
        return "test";
    }
}
