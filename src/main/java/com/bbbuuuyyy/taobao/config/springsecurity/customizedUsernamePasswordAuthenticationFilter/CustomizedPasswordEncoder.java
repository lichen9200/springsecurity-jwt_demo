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
        return new BCryptPasswordEncoder().encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return false;
    }
}
