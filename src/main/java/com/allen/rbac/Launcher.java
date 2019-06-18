package com.allen.rbac;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
// 启动事务管理，等同于xml配置方式的<tx:annotation-driven/>
@EnableTransactionManagement
public class Launcher extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(Launcher.class);
    }

    public static void main(String...args) {
        SpringApplication.run(Launcher.class, args);
    }
}
