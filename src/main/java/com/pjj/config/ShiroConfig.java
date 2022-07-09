package com.pjj.config;

import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Subject：主体，代表了当前 “用户”, 这个用户不一定是一个具体的人, 与当前应用交互的任何东西都是 Subject,
 * 所有 Subject 都绑定到 SecurityManager,与 Subject 的所有交互都会委托给 SecurityManager
 *
 * SecurityManager：安全管理器; 即所有与安全有关的操作都会与 SecurityManager 交互;且它管理着所有 Subject;它是 Shiro 的核心,
 * 它负责与他组件进行交互，可以把它看成 DispatcherServlet 前端控制器
 *
 * Realm：域，Shiro 从 Realm 获取安全数据（如用户、角色、权限），就是说 SecurityManager 要验证用户身份,那么它需要从 Realm 获取相应的用户进行比较
 * 以确定用户身份是否合法; 也需要从 Realm 得到用户相应的角色 / 权限进行验证用户是否能进行操作;可以把 Realm 看成 DataSource，即安全数据源
 */
@Configuration
public class ShiroConfig {

    //ShiroFilterFactoryBean
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(@Autowired DefaultWebSecurityManager defaultWebSecurityManager){
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(defaultWebSecurityManager);//设置安全管理器

        /**
         * 设置Shiro的内置过滤器
         * anon: 无需认证即可以访问
         * authc: 必须认证了才可以访问
         * user: 必须拥有 记住我 功能才可以用
         * perms: 拥有对 某个资源的权限才可以访问
         * role: 拥有某个角色权限才能访问
         */
        Map<String, String> map = new LinkedHashMap<>();
        //授权(设置页面访问权限)
        //需要有 root权限 才可以访问 /admin路径下面的页面
        map.put("/admin","anon");       //跳转去登录页面的路径不拦截
        map.put("/admin/login","anon"); //请求登录的路径不拦截
        map.put("/admin/**","perms[admin]");// ? 匹配任意的一个字符, * 匹配一个或者多个任意的字符, ** 匹配零个或者多个目录

        map.put("/","anon");
        map.put("/types","anon");
        map.put("/tags","anon");
        map.put("/archives","anon");
        map.put("/about","anon");
        map.put("/blog/*","anon");

        shiroFilterFactoryBean.setFilterChainDefinitionMap(map);//设置Shiro的内置过滤器链

        //设置登录页面  (默认自动跳转的登录页面为/login.jsp)
        shiroFilterFactoryBean.setLoginUrl("/admin");
        //设置未授权页面   (没有设置,那么用户访问该用户没有权限访问的页面时,会报401等错误,  设置后,会跳转到设置的未授权页面)
        shiroFilterFactoryBean.setUnauthorizedUrl("/error/withoutPermission");

        return shiroFilterFactoryBean;
    }

    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager(@Autowired UserRealm userRealm){
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);//关联UserRealm
        return securityManager;
    }

    //创建 realm 对象 , 自定义
    @Bean
    public UserRealm userRealm(){
        return new UserRealm();
    }
}
