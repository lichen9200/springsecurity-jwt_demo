package com.bbbuuuyyy.taobao.config;
/*springSecurity配置文件


 */


import com.bbbuuuyyy.taobao.config.oneperrequestfilter.CustomOncePerRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CorsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity //整合springMVC和提供web安全支持服务
@Configuration
//如何保证这个配置能对目的组件生效？
//如何保证项目中这个依赖会在适合的时候被调用？？
public class WebSecurityConfig {
    @Autowired
    LoginFilter loginFilter;
    @Autowired
    CustomOncePerRequestFilter customOncePerRequestFilter;

    @Autowired
    CustomizedAuthenticationEntryPoint customizedAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        重放原始请求
        HttpSessionRequestCache requestCache = new HttpSessionRequestCache();
        requestCache.setMatchingRequestParameterName("continue");
        http
                // ...重放请求???
                .requestCache((cache) -> cache
                        .requestCache(requestCache)
                )

                .authorizeHttpRequests((requests) -> requests
                                //如何是2.0的springboot版本就有类型不匹配问题，要3.1.0以后的吧
                                .requestMatchers("/", "/home").permitAll()
                                .anyRequest().authenticated()

                        //意思是根路径和home不需要验证，而其他任何路径都要登录后才能访问
                )
                //跨域请求设置，待编辑。。。？？？？？？？？
                //跨域：协议，域名，端口三个之一不同则为跨域
      .cors().and().csrf().disable()

                //搞不懂这个，好像是没有登录就重定向？
                //用lamdba代替链式配置，还是一样含义
                //基本原理就是请求先到过滤器，过滤器能匹配的，过滤器被调用处理，没有匹配的就到MVC处理

                // 禁用basic明文验证
                .httpBasic().disable()
                // 前后端分离架构不需要csrf保护,有的话，要有个csrf的值才能正常请求
                .csrf().disable()
                // 禁用默认登录页???其实是禁用了表单登录,弱智..!!!!直接写一个新的覆盖就行啊....
                //.formLogin().disable()
                .formLogin((form) -> form
                        .loginPage("/login") //请求路径相同时，覆盖了security的自带/login
                        .permitAll()
                )
                // 禁用默认登出页
                //.logout().disable()



                // 设置异常的EntryPoint，如果不设置，默认使用Http403ForbiddenEntryPoint
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(customizedAuthenticationEntryPoint))
                // 前后端分离是无状态的，不需要session了，直接禁用。
                //.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               // .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                        // 允许所有OPTIONS请求
                       // .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // 允许直接访问授权登录接口
                        //.requestMatchers(HttpMethod.POST, "/web/authenticate").permitAll()
                        // 允许 SpringMVC 的默认错误地址匿名访问
                        //.requestMatchers("/error").permitAll()
                        // 其他所有接口必须有Authority信息，Authority在登录成功后的UserDetailsImpl对象中默认设置“ROLE_USER”
                        //.requestMatchers("/**").hasAnyAuthority("ROLE_USER")
                        // 允许任意请求被已登录用户访问，不检查Authority
                        //.anyRequest().authenticated())
                //??下面这个什么东西
                //.authenticationProvider(authenticationProvider())


                //整合JWT：
                //1.禁用session
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                //用自定义的验证过滤器替代原来的登录验证过滤器
               .addFilterAt(loginFilter, UsernamePasswordAuthenticationFilter.class)
              .addFilterBefore(customOncePerRequestFilter,UsernamePasswordAuthenticationFilter.class);


//                .logout((logout) -> logout.permitAll());
// 禁用session,使用JWT
//        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        return http.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService() {
//        UserDetails user = User.withDefaultPasswordEncoder()
//                .username("user")
//                .password("pa")
//                .roles("USER")    //可以有多个角色！！
//                .build();
//
//        return new InMemoryUserDetailsManager(user);
//
//
//    }

    @Bean
    //cors配置就是写一个配置类，然后上面写一个cors（）就行，应该会自动调用？？？
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*"); // 允许的源，这里使用通配符*表示允许所有源
        configuration.addAllowedMethod("*"); // 允许的HTTP方法，这里使用通配符*表示允许所有方法
        configuration.addAllowedHeader("*"); // 允许的HTTP头，这里使用通配符*表示允许所有头

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // 配置CORS规则应用于所有URL路径

        return source;
    }


}

