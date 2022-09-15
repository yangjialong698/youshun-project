package com.ennova.pubinfotask;


import com.ennova.pubinfotask.config.SocketBeanManager;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@SpringBootApplication
@MapperScan("com.ennova.pubinfotask.dao")
//@EnableDiscoveryClient
@EnableDiscoveryClient
@EnableFeignClients
// 开启定时任务
@EnableScheduling
public class PubInfoTaskApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(PubInfoTaskApplication.class, args);
        SocketBeanManager.setApplicationContext(applicationContext);
    }

}
