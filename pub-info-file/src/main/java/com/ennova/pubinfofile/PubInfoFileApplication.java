package com.ennova.pubinfofile;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@SpringBootApplication
@MapperScan("com.ennova.pubinfofile.dao")
//@EnableDiscoveryClient
@EnableDiscoveryClient
@EnableFeignClients
public class PubInfoFileApplication {

    public static void main(String[] args) {
        SpringApplication.run(PubInfoFileApplication.class, args);
    }

}
