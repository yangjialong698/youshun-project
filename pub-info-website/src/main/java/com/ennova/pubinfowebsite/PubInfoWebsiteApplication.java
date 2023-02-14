package com.ennova.pubinfowebsite;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@MapperScan("com.ennova.pubinfowebsite.dao")
@EnableTransactionManagement
public class PubInfoWebsiteApplication {

    public static void main(String[] args) {
        SpringApplication.run(PubInfoWebsiteApplication.class, args);
    }

}
