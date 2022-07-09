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

        //就是说 如果@RequestMapping注解的方法报的异常 包含有状态码,就交给springboot默认处理(会跳转到对应状态码的html页面)
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
