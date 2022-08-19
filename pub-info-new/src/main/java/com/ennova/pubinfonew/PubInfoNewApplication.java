package com.ennova.pubinfonew;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.ennova.pubinfonew.dao")
public class PubInfoNewApplication {

    public static void main(String[] args) {
        SpringApplication.run(PubInfoNewApplication.class, args);
    }

}
