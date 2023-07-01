package com.bbbuuuyyy.taobao.config.springsecurity.customizedUsernamePasswordAuthenticationFilter;

import com.bbbuuuyyy.taobao.dao.AuthorMapper;
import com.bbbuuuyyy.taobao.entity.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserServiceImpl implements  UserDetailsService {
    @Autowired
    AuthorMapper authorMapper;
    @Override
    public  UserDetail loadUserByUsername(String username) throws UsernameNotFoundException {
        Author author = authorMapper.selectAuthorByUsername(username);
        if(author.getUsername()!="" && author.getUsername() != null)
        {
            Collection<SimpleGrantedAuthority> authorities =  new ArrayList<SimpleGrantedAuthority>();

            authorities.add(new SimpleGrantedAuthority("user"));
            System.out.println("userDetail被调用。。。。。" + authorities);
            UserDetail userDetail = new UserDetail(author,authorities);
            return userDetail;
        }
        else {return null;}
    }
}
