package com.ennova.pubinfopurchase;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.ennova.pubinfopurchase.dao")
public class PubInfoPurchaseApplication {

    public static void main(String[] args) {
        SpringApplication.run(PubInfoPurchaseApplication.class, args);
    }

}
