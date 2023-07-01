package com.bbbuuuyyy.taobao.config.springsecurity.customizedUsernamePasswordAuthenticationFilter;

import com.bbbuuuyyy.taobao.entity.Author;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
@Data
//security身份验证下的用户对象
public class UserDetail extends User {
    private Author author;

    public UserDetail(Author author, Collection<? extends GrantedAuthority> authorities) {
        //必须调用父类构造方法？以初始化用户名，密码、权限？？

        //子类为什么一定要调用父类构造方法？？？
        super(author.getUsername(),author.getPassword(),authorities);
                this.author = author;
    }

    public Author getAuthor() {
        return author;
    }
}
