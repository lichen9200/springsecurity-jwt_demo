package com.bbbuuuyyy.taobao.config.oneperrequestfilter;

import com.bbbuuuyyy.taobao.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.autoconfigure.neo4j.Neo4jProperties;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
/*
跟另外一个过滤器一起使用有冲突，不能正常访问，妈的。。。。还是因为添加到logout前而出错，因为logout已经被disable，所以是前后矛盾？？？
应该 httpSecurity.addFilterBefore(jwtAuthenticationFilter, LogoutFilter.class)
这样保证在退出前获得authencation，有人说不好？？有瑕疵？

 */

//extends OncePerRequestFilter导致出错。。。。页面没有报错，什么都没报错，响应200，就是返回空白页面，
// 就算注释，不添加该filter到某过滤器前，也会出错，所以应该跟springsecurity对过滤器的自动配置有关
@Component
public class CustomOncePerRequestFilter extends OncePerRequestFilter{

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        System.out.println("被调用...");
        /*
        1.刷新一次页面被调用多次,应该是登录页和其他过滤器的配置混乱问题,导致多次发起请求,
        应该是在过滤器器链里面,有个多次请求.??????????????
        2.怎么搭配登录页,登录页get请求不能正常响应,而是循环调用该类了....????
         */
        //应该是在过滤器器链里面,有个多次请求.??????????????
        String jwtToken = request.getHeader("authorization");//改。。。。
        //1.首先接收jwt，解析，要不要放入上下文？无状态，应该不用存、
        //若满足：未过期，有请求所需对应权限，则放行（如何表示放行？）
        if (jwtToken != null && jwtToken != "") {
            System.out.println("查出来，放行。。。。" + jwtToken);
            if(JwtUtil.validateToken(jwtToken))
            {
                System.out.println("jwttoken可以正常使用...");
                String username = JwtUtil.extractUsername(jwtToken);
                System.out.println(username);
                System.out.println(JwtUtil.extractExpirationDate(jwtToken));
                Authentication authentication = new UsernamePasswordAuthenticationToken(username,null,null);


                //已经登录的标志!!!放入到上下文里面了...
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("登录成功,欢迎" + username);

            }


            filterChain.doFilter(request, response);


        } else {
            System.out.println("没有登录...");
            System.out.println(request.getMethod());
            System.out.println(request.getRequestURI());
            filterChain.doFilter(request, response);//这个不知如何理解...??


        }
    }
}
