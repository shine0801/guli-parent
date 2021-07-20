package com.atguigu.ucenterservice;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan({"com.atguigu"})
@SpringBootApplication//取消数据源自动配置
@EnableDiscoveryClient
@MapperScan("com.atguigu.ucenterservice.mapper")
public class UCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(UCenterApplication.class, args);
    }
}
