package com.atguigu.oss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
@ComponentScan(basePackages = {"com.atguigu"}) //设置包的扫描规则，如果设置，只能扫描当前项目的包
public class OssApplication {
    public static void main(String[] args) {
            SpringApplication.run(OssApplication.class, args);
    }
}


