package com.pjj.controller.admin;

import com.pjj.entity.User;
import com.pjj.service.UserService;
import com.pjj.uitl.MD5Utils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class LoginController {

    @Autowired
    private UserService userService;

    //跳转到登录页面
    @GetMapping //没有写路径,那么访问该类上面的路径时就会跳转到该方法.
    public String loginPage(){
        return "admin/login";
    }

    //登录
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(@RequestParam("username") String username,
                        @RequestParam("password") String password,
                        HttpSession session,
                        RedirectAttributes attributes){
        //shiro获取当前的用户
        Subject subject = SecurityUtils.getSubject();

        //封装用户的登录数据
        UsernamePasswordToken token = new UsernamePasswordToken(username, MD5Utils.md5Encode(password.getBytes()));

        try{
            subject.login(token); //执行登录方法, 如果没有异常就说明ok了    (执行登录方法会跳到Realm中的doGetAuthenticationInfo()方法)

            Subject currentSubject = SecurityUtils.getSubject();
            User currentUser = (User)subject.getPrincipal();//获取当前登录成功的用户
            Session shiroSession = currentSubject.getSession();//获取session对象 这个session是Shiro的session不是  Servlet中的HttpSession
            shiroSession.setAttribute("userLogin",currentUser);//将用户放入session
            //不管是通过 request.getSession或者subject.getSession获取到session，操作session，两者都是等价的。
            //在使用默认session管理器的情况下，操作session都是等价于操作HttpSession。

            session.setAttribute("user",currentUser);//唉,搞不清httpSession与Shiro的session,还是把两个加入吧,注销时也两个都注销
            return "admin/index";//登录成功, 跳转到主页
        } catch (UnknownAccountException e){//用户名不存在 会报该异常
            e.printStackTrace();
            attributes.addFlashAttribute("msg","用户名不存在");
            return "redirect:/admin";//用户名不存在就 跳转到登录页面login
        } catch (IncorrectCredentialsException e){//密码不存在
            e.printStackTrace();
            attributes.addFlashAttribute("msg","密码错误");
            return "redirect:/admin";//密码错误, 跳转到登录页面login
        }
    }

    //注销
    @GetMapping("/logout")
    public String logout(HttpSession session){
        Subject currentUser = SecurityUtils.getSubject();
        currentUser.logout();//shiro的注销
        //session.removeAttribute("user");//httpsession的注销   报错了,报会话以无效,可以shiro的注销,会自动清空所有session吧
        return "redirect:/admin";
    }

    @RequestMapping("/index")
    public String index(){
        return "admin/index";
    }
}
