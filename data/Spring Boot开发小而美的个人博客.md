# Spring Boot开发小而美的个人博客

> ***作者: LaoPeng***

> ***日期: 2020/6/27 14::30始   ~~~   2020/7/16 10:40止***
>
> ***历时20天		该课程视频一共20个小时 平均一天一个小时吧   其实两个星期就可以完成了的, 是后来懈怠了, 唉！***

# 1、需求与功能

个人博客系统的用户故事:

角色: 普通访客， 管理员(我)

- 访客，可以分页查看所有博客

* 访客，可以快速查博客数最多的6个分类
* 访客，可以查看所有的分类
* 访客，可以查看某个分类下的博客列表
* 访客，可以快速查看标记博客最多的10个标签
* 访客，可以查看所有的标签
* 访客，可以查看某个标签下的博客列表
* 访客，可以根据年度时间线查看博客列表
* 访客，可以快速查看最新的推荐博客
* 访客，可以用关键字全局搜索博客
* 访客，可以查看单个博客内容
* 访客，可以对博客内容进行评论
* 访客，可以赞赏博客内容
* 访客，可以微信扫码阅读博客内容
* 访客，可以在首页扫描公众号二维码关注我
* 
* 我，可以用户名和密码登录后台管理
* 我，可以管理博客
  * 我，可以发布新博客
  * 我，可以对博客进行分类 
  * 我，可以对博客打标签
  * 我，可以修改博客
  * 我，可以删除博客
  * 我，可以根据标题，分类，标签查询博客

* 我，可以管理博客分类
  * 我，可以新增一个分类
  * 我，可以修改一个分类
  * 我，可以删除一个分类
  * 我，可以根据分类名称查询分类

* 我，可以管理标签
  * 我，可以新增一个标签
  * 我，可以修改一个标签
  * 我，可以删除一个标签
  * 我，可以根据名称查询标签
    

## 1.1 功能规划

<img src="D:\Spring Boot开发小而美的个人博客\笔记\img\x.png" />



# 2、页面设计与开发



## 2.1设计

前端展示：首页、详情页、分类、标签、归档、关于我

后台管理：模板页

 

## 2.3插件集成

<a href="https://pandao.github.io/editor.md/">编辑器 Markdown</a>

<a href="https://github.com/sofish/typo.css">内容排版 typo.css</a>

<a href="https://animate.style/">动画 animate.css</a>

<a href="https://prismjs.com/">代码高亮 prism</a>

<a href="https://tscanlin.github.io/tocbot/">目录生成 Tocbot</a>

<a href="http://imakewebthings.com/waypoints/">滚动侦测 waypoints</a>

<a href="https://github.com/flesler/jquery.scrollTo">平滑滚动 jquery.scrollTo</a>

<a href="https://davidshimjs.github.io/qrcodejs/">二维码生成 qrcode.js</a>

> ***日期: 2020/7/4 静态页面完成	2020/6/27~2020/7/4 用了7天***



# 3、框架搭建

> ***日期: 后台于2020/7/4始***

## 3.1构建与配置

1、引入Spring Boot模块:

- web
- Thymeleaf
- MyBatis (老师用的是 JPA)
- MySQL
- Aspects
- DevTools

## 3.2异常处理

**1、定义错误页面**

* 404
* 500
* error

**2、全局处理异常**

统一处理异常

```java
package com.pjj.controller;

import com.pjj.MyBlogApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
//@ControllerAdvice，是Spring3.2提供的新注解,它是一个Controller增强器,可对controller中被 @RequestMapping注解的方法加一些逻辑处理。最常用的就是异常处理
//也就是Controller中的@RequestMapping注解的方法报异常了之后,会跳转到该类中由相应方法的处理
//被@ExceptionHandler(不同类型的异常类.class)注解了的方法会处理某一类异常,然后进行处理,返回返回
public class ExceptionController {

    private Logger logger = LoggerFactory.getLogger(MyBlogApplication.class);

    //等于说 所有@RequestMapping注解的方法 报异常了都会跑到这个方法里,因为这个方法是处理Exception,Exception是最大的父类(Throwable异常可能不会到这里处理吧)
    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request,Exception e) throws Exception {
        logger.error("Request URL : {}, Exception : {}",request.getRequestURL(),e);

        //如果@RequestMapping注解的方法报的异常 包含有状态码,就交给springboot默认处理(会跳转到对应状态码的html页面)
        //如果没有状态码,就由我们自己写的这个方法来处理 比如此处是跳转到 error.html页面
        if(AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null){
            //findAnnotation(e.getClass(), ResponseStatus.class) 判断e这个异常 有没有 状态码,如果!=null也就是有状态码,则throw e抛出交給springboot处理
            throw e;
        }

        ModelAndView mv = new ModelAndView();
        mv.addObject("url",request.getRequestURL());
        mv.addObject("exception",e);
        mv.setViewName("error/error");

        return mv;
    }

}
```

## 3.3日志处理

**1、记录日子内容**

* 请求url
* 访问者ip
* 调用方法classMethod
* 参数args
* 返回内容

**2、记录日志类**

```java
package com.pjj.aspect;

import com.pjj.MyBlogApplication;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;

@Aspect
@Component
public class LogAspect {

    private final Logger logger = LoggerFactory.getLogger(MyBlogApplication.class);

    /**
     * 定义切点,以前是在每个 前置通知或后置通知...上加如@Before或@AfterReturning等等...注解时,在每个注解上写pointcut = "execution("....")"
     * 然后前置通知方法(切面)就会在execution("...")中指定的(切点)执行前,执行前置通知方法.
     * 现在只是
     *      定义一个方法, 用于声明切入点表达式. 一般地, 该方法中再不需要添入其他的代码.
     *     使用 @Pointcut 来声明切入点表达式. 后面的其他通知直接使用方法名来引用当前的切入点表达式.
     */
    @Pointcut("execution(* com.pjj.controller.*.*(..))")
    public void log(){}

    @Before("log()")
    public void doBefore(JoinPoint joinPoint){
        //获取到request对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURL().toString();
        String ip = request.getRemoteAddr();
        String classMethod = joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName();//类名.方法名
        Object[] args = joinPoint.getArgs();
        RequestLog requestLog = new RequestLog(url, ip, classMethod, args);
        logger.info("Request : {}",requestLog);
    }

    @AfterReturning(pointcut = "log()",returning = "result")//只有一个参数,可以省略pointcut直接写"log()"
    public void doAfterReturn(Object result){//定义返回值后,需要在注解上声明一下returning = "result"
        logger.info("Result : {}",result);
    }

//    @After("log()")
//    public void doAfter(){
//        logger.info("------doAfter------");
//    }

    private class RequestLog {
        private String url;
        private String ip;
        private String classMethod;
        private Object[] args;

        public RequestLog() {
        }
        public RequestLog(String url, String ip, String classMethod, Object[] args) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
            this.args = args;
        }

        @Override
        public String toString() {
            return '{' +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    ", args=" + Arrays.toString(args) +
                    '}';
        }
    }


}
```

## 3.4页面处理

**1、将之前写好的静态页面导入Project**

**2、thymeleaf布局**

* 定义fragment
* 使用fragment布局

**3、错误页面美化**



# 4、设计与规范

## 4.1实体设计

**实体类**

* 博客 Blog
* 博客分类 Type
* 博客标签 Tag
* 博客评论 Comment
* 用户 User

**实体关系**

<img src="C:\Users\彭家俊\Desktop\实体关系.png" />

一篇博客只有一个分类, 一个分类对应多篇博客				博客 与 分类  多对一			分类 与 博客   一对多

一篇博客有多个标签, 一个标签也有多个博客					博客 与 标签  多对多

一篇博客只能对应一个用户, 一个用户可以有多篇博客	博客 与 用户   多对一			用户 与 博客   一对多

一篇博客可能有很多评论, 一个评论只属于一片博客		博客 与 评论   一对多			评论 与 博客   多对一



**评论类自关联关系**

<img src="D:\Spring Boot开发小而美的个人博客\笔记\img\评论类自关联关系.png" />



**Blog类**

<img src="D:\Spring Boot开发小而美的个人博客\笔记\img\Blog类.png" />

Type类

<img src="D:\Spring Boot开发小而美的个人博客\笔记\img\Type类.png" />

Tag类

<img src="D:\Spring Boot开发小而美的个人博客\笔记\img\Tag类.png" />

Comment类

<img src="D:\Spring Boot开发小而美的个人博客\笔记\img\Comment类.png" />

User类

<img src="D:\Spring Boot开发小而美的个人博客\笔记\img\User类.png" />



## 4.2应用分层

<img src="D:\Spring Boot开发小而美的个人博客\笔记\img\应用分层.png" />

## 4.3命名约定

**Service/DAO层命名约定:**

* 获取单个对象的方法用get做前缀

* 获取多个对象的方法用list做前缀

* 获取统计值的方法用count做前缀

* 插入的方法用save(推荐) 或 insert做前缀

* 删除的方法用remove(推荐) 或 delete做前缀

* 修改的方法用update做前缀

  

# 5、后台管理

## 5.1登录管理

1. 构建登录页面和后台管理页面
2. UserService和UserDAO
3. LoginController实现登录
4. MD5加密
5. 登录拦截器



## 5.2分类管理

1. 分类管理页面
2. 分类列表分页
3. 分类新增、修改、删除



## 5.3标签管理

1. 标签管理页面
2. 标签列表分页
3. 标签新增、修改、删除

## 5.4博客管理

1. 博客分页查询
2. 博客新增
3. 博客修改
4. 博客删除



# 6、前端展示



## 6.1首页展示

1. 博客列表

2. top分类

3. top标签

4. 最新博客推荐

5. 博客详情

   * Markdown转换HTML

   * <a href="https://github.com/atlassian/commonmark-java">commonmark-java</a>

   * pom.xml引用commonmark和扩展插件

   * ```xml
     <!-- 将Markdown语法转为HTML -->
     <dependency>
     	<groupId>com.atlassian.commonmark</groupId>
         <artifactId>commonmark</artifactId>
     	<version>0.15.0</version>
     </dependency>
     
     <!-- 将Markdown语法转为HTML的一个扩展 可以更好的展示表格标签table -->
     <dependency>
     	<groupId>com.atlassian.commonmark</groupId>
     	<artifactId>commonmark-ext-gfm-tables</artifactId>
         <version>0.15.0</version>
     </dependency>
     
     <!-- 将Markdown语法转为HTML的一个扩展 可以更好的展示h1~h7这种标签，生成id, 最后可以根据生成的id生成文章目录 -->
     <dependency>
         <groupId>com.atlassian.commonmark</groupId>
         <artifactId>commonmark-ext-heading-anchor</artifactId>
         <version>0.10.0</version>
     </dependency>
     ```

     
     

## 6.2评论功能

* 评论信息提交与回复功能

* 评论信息列表展示功能

* 管理员回复评论功能

  

## 6.3分类页

## 6.4标签页

## 6.5归档页

## 6.6关于我

