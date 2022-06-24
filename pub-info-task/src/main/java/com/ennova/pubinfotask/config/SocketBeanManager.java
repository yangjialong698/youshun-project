package com.ennova.pubinfotask.config;


import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;


@Component
public class SocketBeanManager {

    static ApplicationContext applicationContext;

    public static void setApplicationContext(ApplicationContext context) {
        applicationContext = context;
    }

}
