package com.bbbuuuyyy.taobao.entity;

import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
@Data
public class JwtAndAuthorities implements Authentication {
    //应该保护一个token，和权限信息
    private String jwtToken;
    //权限信息，要按权限还是角色？？？
    private Collection<GrantedAuthority> authorities;

    private String username;

    public JwtAndAuthorities(String username, String jwtToken, Collection<GrantedAuthority> authorities) {
        this.jwtToken = jwtToken;
        this.authorities = authorities;
        this.username = username;
    }


    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return null;
    }

    @Override
    public boolean isAuthenticated() {
        return false;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

    }

    @Override
    public String getName() {
        return null;
    }
}
