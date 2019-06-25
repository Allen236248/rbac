package com.allen.rbac.configuration;

import com.allen.rbac.exception.CustomHandlerExceptionResolver;
import com.allen.rbac.util.MybatisLogInterceptor;
import com.allen.rbac.util.MybatisPaginationInterceptor;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return new ConfigurationCustomizer() {

            @Override
            public void customize(org.apache.ibatis.session.Configuration configuration) {
                configuration.addInterceptor(new MybatisLogInterceptor());
                configuration.addInterceptor(new MybatisPaginationInterceptor());
            }
        };
    }

    @Bean
    public CustomHandlerExceptionResolver exceptionHandler(){
        return new CustomHandlerExceptionResolver();
    }
}
