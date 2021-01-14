package com.qf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Created by 54110 on 2020/12/29.
 */
@SpringBootApplication(exclude =  org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class)
@EnableDiscoveryClient
public class HouseSearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(HouseSearchApplication.class);
    }
}
