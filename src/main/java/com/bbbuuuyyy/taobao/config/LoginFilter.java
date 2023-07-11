package com.bbbuuuyyy.taobao.config;

import com.bbbuuuyyy.taobao.config.springsecurity.customizedUsernamePasswordAuthenticationFilter.CustomizedAuthenticationManager;
import com.bbbuuuyyy.taobao.entity.JwtAndAuthorities;
import com.bbbuuuyyy.taobao.util.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.log.LogMessage;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
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


    //一定要自定义，不然肯定为null
    public LoginFilter(AuthenticationManager customizedAuthenticationManager) {
        // super(customizedAuthenticationManager);\
        //必须将验证的类传递给父类，才能实现：调用自己定义的验证类。
        super(customizedAuthenticationManager);
        this.setAuthenticationManager(customizedAuthenticationManager);
        ;
    }

    @Override
    //AuthenticationManager似乎从来没有实例化，如何能使用到？
    //底层自动拦截过滤，/login的post请求
    //@ResponseBody
//    好像跟返回json无关。。
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("LoginFilter的attemptAuthentication被调用了。。。。。");
        System.out.println("authMager" + this.getAuthenticationManager());
        System.out.println(" " + request.getContentType());
        if (request.getContentType().equals(MediaType.APPLICATION_JSON_UTF8_VALUE)
                || request.getContentType().equals(MediaType.APPLICATION_JSON_VALUE)) {
            System.out.println("进来里面了。。。");
            ObjectMapper mapper = new ObjectMapper();
            UsernamePasswordAuthenticationToken authRequest = null;
            try (InputStream is = request.getInputStream()) {
                Map<String, String> authenticationBean = mapper.readValue(is, Map.class);
                authRequest = new UsernamePasswordAuthenticationToken(
                        authenticationBean.get("username"), authenticationBean.get("password"));
            } catch (IOException e) {
                e.printStackTrace();
                authRequest = new UsernamePasswordAuthenticationToken(
                        "", "");
            } finally {
                setDetails(request, authRequest);
                return this.getAuthenticationManager().authenticate(authRequest);
            }
        } else {
            System.out.println("不太ok，不是json的，请求父类。。。");
            //对于这种返回方法感到疑惑。。。。
            Authentication authResult = super.attemptAuthentication(request, response);
            System.out.println(authResult);
            SecurityContextHolder.getContext().setAuthentication(authResult);
            if (authResult.isAuthenticated() == true) {
                //this.successfulAuthentication(request,response,chain,authResult);
                return authResult; //
                //super.successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult);
            }
            return null;

            //就返回这点破东西回去？？？？
            //还是转到 原来的地方。。。所以设置服务器的上下文，不能使客户端能够进入登录状态？？？
        }
    }

    @ResponseBody

    @Override
    public void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("successfulAuthentication被调了。。。。");

        /*存在问题:1.不能返回jwt,返回值为void,噢,jwt应该放在请求头.那么客户端如何自己保存起来.
                    2.没有转发到之前的请求页面.官网有
                    3.记住我是什么东西,免登录功能,类似jwt
                    4.如何在叫客户端存储jwt,靠前端逻辑js,要自己写.....
        * */
        /*
        1.还差一个对jwtToken进行验证的类
        * */
        //1.登录成功则将auth放入上下文中
        SecurityContextHolderStrategy securityContextHolderStrategy = SecurityContextHolder.getContextHolderStrategy();
        SecurityContext context = securityContextHolderStrategy.createEmptyContext();
        context.setAuthentication(authResult);
        securityContextHolderStrategy.setContext(context);
        System.out.println(" " + authResult.getPrincipal() + authResult.getCredentials() + " " + authResult.getAuthorities());
        String jwtToken =  JwtUtil.generateToken(authResult);
        System.out.println(jwtToken);
        //新版jdk加载该类,需要在pom导入新依赖,或者直接使用jdk8.
        //响应头，字段名不区分大小写，但是建议统一为首字母小写
        //无语，服务器响应头字段大写，axios显示的居然又是小写。。且小写，axios才能正常获取authication的值。.虽然在数据包的响应标头里面显示的是大写。。
        //所以使用axios的情况下，统一小写。。。
        response.addHeader("Access-Control-Expose-Headers","Authorization");
        response.setHeader("authorization", jwtToken);


//        this.securityContextRepository.saveContext(context, request, response);
//        if (this.logger.isDebugEnabled()) {
//            this.logger.debug(LogMessage.format("Set SecurityContextHolder to %s", authResult));
//        }
//
//        this.rememberMeServices.loginSuccess(request, response, authResult);
//        if (this.eventPublisher != null) {
//            this.eventPublisher.publishEvent(new InteractiveAuthenticationSuccessEvent(authResult, this.getClass()));
//        }
//
//        this.successHandler.onAuthenticationSuccess(request, response, authResult);
//
//    }

    }
//好像没有调用到实现类啊？？
//class CustomizedAuthenticationManager extends AuthenticationManager

}