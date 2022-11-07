package com.ennova.pubinfoproduct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PubInfoProductApplication {

    public static void main(String[] args) {
        SpringApplication.run(PubInfoProductApplication.class, args);
    }

}
