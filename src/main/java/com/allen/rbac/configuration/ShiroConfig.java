package com.allen.rbac.configuration;

import com.allen.rbac.shiro.CustomShiroRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher matcher = new HashedCredentialsMatcher();
        //散列算法
        matcher.setHashAlgorithmName("MD5");
        //散列次数。比如2就相当于md5(md5(""))
        matcher.setHashIterations(1);
        return matcher;
    }

    @Bean
    public CustomShiroRealm customShiroRealm(HashedCredentialsMatcher hashedCredentialsMatcher) {
        CustomShiroRealm customShiroRealm = new CustomShiroRealm();
        customShiroRealm.setCredentialsMatcher(hashedCredentialsMatcher);
        return customShiroRealm;
    }

    @Bean
    public SecurityManager securityManager(CustomShiroRealm customShiroRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(customShiroRealm);
        //securityManager.setSessionManager();
        // securityManager.setCacheManager();
        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);
        //shiroFilterFactoryBean.setFilters();

        //过滤器链
        Map<String, String> filterChainDefinitions = new LinkedHashMap<>();
        //静态资源配置使用匿名拦截器表示不被过滤
        filterChainDefinitions.put("/static/**", "anon");

        //退出操作使用logout过滤器
        filterChainDefinitions.put("/to_logout", "logout");

        //登陆使用authc过滤器。authc表示认证（登陆）过滤器：需要认证才能访问
        filterChainDefinitions.put("/to_login", "authc");

        //过滤器链的定义是从上至下的顺序执行的，所以/**必须放到最下面。user过滤器（访问控制）：配置记住我或认证通过后才可以访问
        filterChainDefinitions.put("/**", "user");
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitions);

        //如果不配置，默认自动寻找web根目录下的/login.jsp页面
        shiroFilterFactoryBean.setLoginUrl("/login");
        //登陆成功后跳转的链接
        shiroFilterFactoryBean.setSuccessUrl("/home");
        //未授权的界面
        shiroFilterFactoryBean.setUnauthorizedUrl("/403");
        return shiroFilterFactoryBean;
    }

    @Bean
    public LifecycleBeanPostProcessor lifecycleBeanPostProcessor(){
        return new LifecycleBeanPostProcessor();
    }
}
