package com.bbbuuuyyy.taobao.config;

import com.bbbuuuyyy.taobao.config.springsecurity.customizedUsernamePasswordAuthenticationFilter.CustomizedAuthenticationManager;
import com.bbbuuuyyy.taobao.entity.JwtAndAuthorities;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/*"
/login"&&“post”请求才被调用

 */
@Component
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    @Autowired
    CustomizedAuthenticationManager customizedAuthenticationManager;

    public LoginFilter(AuthenticationManager customizedAuthenticationManager) {
       // super(customizedAuthenticationManager);\
        //必须将验证的类传递给父类，才能实现：调用自己定义的验证类。
        super(customizedAuthenticationManager);
        this.setAuthenticationManager(customizedAuthenticationManager);;
    }

    @Override
    //AuthenticationManager似乎从来没有实例化，如何能使用到？
    //底层自动拦截过滤，/login的post请求
    @ResponseBody
    public JwtAndAuthorities attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("LoginFilter的attemptAuthentication被调用了。。。。。");
        System.out.println(" "+request.getContentType());
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)
                || request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            System.out.println("进来里面了。。。");
            ObjectMapper mapper = new ObjectMapper();
            UsernamePasswordAuthenticationToken authRequest = null;
            try (InputStream is = request.getInputStream()) {
                Map<String,String> authenticationBean = mapper.readValue(is, Map.class);
                authRequest = new UsernamePasswordAuthenticationToken(
                        authenticationBean.get("username"), authenticationBean.get("password"));
            } catch (IOException e) {
                e.printStackTrace();
                authRequest = new UsernamePasswordAuthenticationToken(
                        "", "");
            } finally {
                setDetails(request, authRequest);
                return (JwtAndAuthorities) this.getAuthenticationManager().authenticate(authRequest);
            }
        }
        else {
            System.out.println("不太ok，不是json的，请求父类。。。");
            //对于这种返回方法感到疑惑。。。。
            return (JwtAndAuthorities) super.attemptAuthentication(request, response);
        }
    }
}
//好像没有调用到实现类啊？？
//class CustomizedAuthenticationManager extends AuthenticationManager
