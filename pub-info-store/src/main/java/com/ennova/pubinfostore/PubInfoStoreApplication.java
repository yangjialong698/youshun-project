package com.ennova.pubinfostore;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
@MapperScan("com.ennova.pubinfostore.dao")
public class PubInfoStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(PubInfoStoreApplication.class, args);
    }

}
