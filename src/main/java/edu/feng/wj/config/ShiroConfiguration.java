package edu.feng.wj.config;

import edu.feng.wj.filter.URLPathMatchingFilter;
import edu.feng.wj.realm.WJRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @program: wj
 * @description: shiro配置类
 * @author: feng
 * @create: 2020-04-25 10:06
 */
@Configuration
public class ShiroConfiguration {

    // 获取生命周期处理者
    @Bean
    public static LifecycleBeanPostProcessor getLifecycleBeanProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
        // 自定义过滤器设置 1
        Map<String, Filter> customizedFilter = new HashMap<>();
        // 自定义过滤器设置 2，命名，需在设置过滤路径前
        // 设置自定义过滤器名称为url
        customizedFilter.put("url", getURLPathMatchingFilter());
        // 对管理接口的访问启用自定义拦截（url规则）
        // 自定义过滤器设置 3，设置过滤路径
        filterChainDefinitionMap.put("/api/admin/**", "url");

        // 自定义过滤器设置 4，启用
        // 启动自定义过滤器
        shiroFilterFactoryBean.setFilters(customizedFilter);
        // 这个 authc 即 autentication，是 shiro 自带的过滤器。除了它以外，常用的还有 anon（可匿名访问）、roles（需要角色）、perms（需要权限）等。
        // filterChainDefinitionMap.put("/api/authentication", "authc"); // 防鸡贼登录，暂时不需要

        //  其实使用 perms 才是 Shiro 的祖传解决方案，但是为了配合它的实现，我们需要在配置文件中添加规则如
        //  filterChainDefinitionMap.put("/api/authentication", "perms[/api/admin/user]")，或者在接口处编写注解如
        //  @RequirePermission("/api/admin/user") ，这样如果我们想要删除或新增权限，除了修改数据库外还需要重新编写源码

        // 自带过滤器会拦截 options 请求，所以在前后端分离的项目里使用自定义过滤器反而简便一些。。。
        //
        // 不过，其实对很多项目来说不太需要动态增删权限，只需要对现有权限进行分配就够了，所以在不分离的情况下使用自带过滤器当然更好。
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);

        return shiroFilterFactoryBean;
    }

    @Bean
    public SecurityManager securityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(getWJRealm());
        // 设置记住我功能时要在安全管理处注册
        securityManager.setRememberMeManager(rememberMeManager());
        return securityManager;
    }

    @Bean
    public WJRealm getWJRealm() {
        WJRealm wjRealm = new WJRealm();
        wjRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return wjRealm;
    }

    @Bean
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        hashedCredentialsMatcher.setHashAlgorithmName("md5");
        hashedCredentialsMatcher.setHashIterations(2);
        return hashedCredentialsMatcher;
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        authorizationAttributeSourceAdvisor.setSecurityManager(securityManager);
        return authorizationAttributeSourceAdvisor;
    }

    /**
     * cookie 管理对象 记住我功能
     * @return
     */
    public CookieRememberMeManager rememberMeManager(){
        CookieRememberMeManager cookieRememberMeManager = new CookieRememberMeManager();
        cookieRememberMeManager.setCookie(rememberMeCookie());
        // / cookieRememberMeManager.setCipherKey用来设置加密的Key,参数类型byte[],字节数组长度要求16
        //         // cookieRememberMeManager.setCipherKey(Base64.decode("3AvVhmFLUs0KTA3Kprsdag=="));
        cookieRememberMeManager.setCipherKey("EVANNIGHTLY_WAOU".getBytes());
        return cookieRememberMeManager;
    }

    // 获取URL过滤器 这里不能使用@bean
    public URLPathMatchingFilter getURLPathMatchingFilter(){
        return new URLPathMatchingFilter();
    }

    @Bean
    public Cookie rememberMeCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("rememberMe");
        // 这里设置了cookie的最大存活时间3天
        simpleCookie.setMaxAge(259200);
        System.out.println("maxAge: "+simpleCookie.getMaxAge());
        return simpleCookie;
    }
}