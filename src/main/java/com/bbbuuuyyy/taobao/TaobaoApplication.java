package com.bbbuuuyyy.taobao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;


@SpringBootApplication
public class TaobaoApplication {
//    @Autowired
//  LoginFilter loginFilter;

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext =  SpringApplication.run(TaobaoApplication.class, args);
        System.out.println();

    }

}
