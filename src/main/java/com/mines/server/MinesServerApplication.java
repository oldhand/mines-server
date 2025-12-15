package com.mines.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * 地下矿实时数据接入服务端启动类
 * 实现文档中所有TCP接口和静态数据填报接口
 */
@SpringBootApplication(scanBasePackages = "com.mines.server")
@EnableJpaRepositories(basePackages = "com.mines.server.repository")
public class MinesServerApplication {
    public static void main(String[] args) {
        SpringApplication.run(MinesServerApplication.class, args);
    }
}
