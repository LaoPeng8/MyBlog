package com.pjj.config;

import com.pjj.entity.User;
import com.pjj.mapper.UserMapper;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;

/**
 * Realm：域，Shiro 从 Realm 获取安全数据（如用户、角色、权限），就是说 SecurityManager 要验证用户身份,那么它需要从 Realm 获取相应的用户进行比较
 * 以确定用户身份是否合法; 也需要从 Realm 得到用户相应的角色 / 权限进行验证用户是否能进行操作;可以把 Realm 看成 DataSource，即安全数据源
 */
public class UserRealm extends AuthorizingRealm {

    @Autowired
    UserMapper userMapper;

    //授权
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();//通过这个info对象设置权限.

        //拿到当前登录的这个用户
        Subject subject = SecurityUtils.getSubject();

        //拿到当前登录的user
        User currentUser = (User)subject.getPrincipal();

        //设置权限, 而权限是从当前对象上获取的, 也就是换了个用户登录,权限就不同了.
        info.addStringPermission(currentUser.getPermission());

        return info;
    }

    //认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {

        UsernamePasswordToken userToken = (UsernamePasswordToken)authenticationToken;//封装着有 用户输入的 用户名与密码的对象

        //用户名,密码~ 数据库中取
        User user = null;
        if(userToken.getUsername()!=null && !userToken.getUsername().equals("")){
            user = userMapper.findUserByName(userToken.getUsername());//根据用户输入的用户名 去数据库中查询
            if(user==null){//则没有查找到任何数据
                return null;//则返回null  返回null的意思就是 抛出UnknownAccountException该异常  该异常的意思是 未知的用户名
            }
        }

        //密码认证, 不需要自己做, Shiro来做     通过返回值 new SimpleAuthenticationInfo对象将 数据库中取的密码 传给 Shiro, Shiro来做密码校验是否一致
        return new SimpleAuthenticationInfo(user,user.getPassword(),"");
    }
}
