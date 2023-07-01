package com.bbbuuuyyy.taobao.controller;


import com.bbbuuuyyy.taobao.config.springsecurity.customizedUsernamePasswordAuthenticationFilter.UserDetail;
import com.bbbuuuyyy.taobao.config.springsecurity.customizedUsernamePasswordAuthenticationFilter.UserServiceImpl;
import com.bbbuuuyyy.taobao.entity.Author;
import com.bbbuuuyyy.taobao.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {
    @Autowired
    UserServiceImpl userService;
    @ResponseBody
    @PostMapping("/login")
    public Map loginIn(@RequestBody Author author)
    {
        UserDetail userDetail = userService.loadUserByUsername(author.getUsername());
        System.out.println(userDetail.getAuthor());
        if(userDetail != null)
        {
            Map map = new HashMap();
            String token  = JwtUtil.generateToken(userDetail);
            map.put("token",token);

            return map;
        }

        return null;

        }

//        else {
//            return "用户名或密码错误！";
//        }
        //调用service的查询数据库验证后
        //若正确，返回用户信息，角色权限
        //封装jwt返回给客户端

        //若验证信息错误，返回false，重新跳转登录页面，返回错误字符串信息

    @GetMapping("/login")
    public String loginin()
    {
        System.out.println("返回登录页");
        return "login";

    }
    }

