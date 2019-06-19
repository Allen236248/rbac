package com.allen.rbac.configuration;

import com.allen.rbac.util.MybatisLogInterceptor;
import com.allen.rbac.util.MybatisPaginationInterceptor;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
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
}
