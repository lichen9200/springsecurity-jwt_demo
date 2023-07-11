package com.bbbuuuyyy.taobao.config.springsecurity.customizedUsernamePasswordAuthenticationFilter;

import com.bbbuuuyyy.taobao.entity.JwtAndAuthorities;
import com.bbbuuuyyy.taobao.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

@Component
//拥有authenticate（）验证方法的类，能验证authentication当前登录用户
public class CustomizedAuthenticationManager implements AuthenticationManager {


    @Autowired
    CustomizedPasswordEncoder passwordEncoder;

    @Autowired
    UserServiceImpl userService;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("自定义的验证方法被调用了。。。。");
        System.out.println("说权限集为null，不能通过？？");

        UserDetail userDetail = userService.loadUserByUsername((String) authentication.getPrincipal());
        System.out.println(authentication.getPrincipal());
        if(userDetail != null )
        {
            System.out.println(userDetail);
            System.out.println("charS:" + (CharSequence) authentication.getCredentials());
            String encodedPasswd =  passwordEncoder.encode((CharSequence) authentication.getCredentials());
            System.out.println("输入的密码加密后是：" + encodedPasswd);
            if( passwordEncoder.matches((CharSequence) authentication.getCredentials(),userDetail.getPassword()))
            {
                System.out.println("密码正确。。。。" + userDetail.getAuthorities());
               // String jwtToken = JwtUtil.generate(authentication.getName());
                //不是存放入上下文就相当于登录的吗？？？原来是框架根据返回的对象进行判断。。。。是否登录成功。。。
                //应该如何实现？登录成功后，跳转到之前请求的页面？是转发还是重定向？？？
               Authentication hasLoginINauthentication =  new UsernamePasswordAuthenticationToken(authentication.getPrincipal(),authentication.getCredentials(),authentication.getAuthorities());
                //SecurityContextHolder.getContext().setAuthentication(authentication)

               //JwtAndAuthorities jwtAndAuthorities =  new JwtAndAuthorities((String)authentication.getPrincipal(),jwtToken,userDetail.getAuthorities());
               return hasLoginINauthentication;




            }

        }
        System.out.println(authentication.getCredentials());

//单点登录是？首先登录成功，拿到一个jwt，请求的接口肯定不会是login，那么应该登录过滤器不会被触发
        //应该在登录验证前有一个解析过滤器？？
//1.如何确定为登录后？
        //2.如何解析jwt信息？



       return null;
    }
}
