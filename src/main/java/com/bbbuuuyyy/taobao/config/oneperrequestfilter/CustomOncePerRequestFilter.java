package com.bbbuuuyyy.taobao.config.oneperrequestfilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
/*
应该 httpSecurity.addFilterBefore(jwtAuthenticationFilter, LogoutFilter.class)
这样保证在退出前获得authencation，有人说不好？？有瑕疵？

 */
@Component
public class CustomOncePerRequestFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwtToken = request.getHeader("Authorization");
        //1.首先接收jwt，解析，要不要放入上下文？无状态，应该不用存、
        //若满足：未过期，有请求所需对应权限，则放行（如何表示放行？）
        if( jwtToken != null && jwtToken != "")
        {
            System.out.println("查出来，放行。。。。");
            filterChain.doFilter(request,response);

        }

    }
}
