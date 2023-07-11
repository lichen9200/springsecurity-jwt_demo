package com.bbbuuuyyy.taobao.config.springsecurity.customizedUsernamePasswordAuthenticationFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;


/*
SpringSecurity部分的密码加密器

加密
匹配
两个方法。。

 */
@Component
public class CustomizedPasswordEncoder implements PasswordEncoder {
    @Override

    public String encode(CharSequence rawPassword) {
        //直接调用内置的加密器
        //每次密码加密后结果都不同。。这么牛。。。要使用配套的比对方法。。
        return new BCryptPasswordEncoder().encode(rawPassword);
    }


    //是使用用户输入的原生密码和数据库存放的加密后的密码比对
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return new BCryptPasswordEncoder().matches(rawPassword,encodedPassword);
    }
}
