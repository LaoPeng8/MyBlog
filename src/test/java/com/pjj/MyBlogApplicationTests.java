package com.pjj;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pjj.entity.*;
import com.pjj.mapper.BlogMapper;
import com.pjj.mapper.CommentMapper;
import com.pjj.mapper.TagMapper;
import com.pjj.mapper.TypeMapper;
import com.pjj.service.BlogService;
import com.pjj.service.TypeService;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@SpringBootTest
class MyBlogApplicationTests {

    Logger logger = LoggerFactory.getLogger(MyBlogApplication.class);//日志对象

    @Autowired
    TypeMapper typeMapper;

    @Autowired
    TypeService service;

    @Autowired
    TagMapper tagMapper;

    @Autowired
    BlogMapper blogMapper;
    @Autowired
    BlogService blogService;
    @Autowired
    CommentMapper commentMapper;



    @Test
    void contextLoads() {
        logger.trace("trace***********");
        logger.debug("debug***********");
        logger.info("info*************");
        logger.warn("warn*************");
        logger.error("error***********");
    }

    @Test
    void testSelectAll(){
        List<Type> allType = typeMapper.getAllType();
        System.out.println(allType);
    }

    @Test
    void testPage(){

        //加入分页功能  这是实现的方式二  jdk8 lambda用法
        Page<Type> page = service.getTypeWithPagePlus(1, 5);
        String s = page.toString();
//        System.out.println(s);
        System.out.println("当前滴"+page.getPageNum()+"页");
        System.out.println("总页码+"+page.getPages()+"页");

        int i = service.countType()/50000;
        int j = service.countType()%50000;
        System.out.println("iiiiiiiiiii"+i);
        System.out.println("jjjjjjjjjjj"+j);
    }

    @Test
    void testAutoId(){
        int i = typeMapper.saveType(new Type("十一十一十一十一"));
        System.out.println("+++++++++++++++++++++"+i);
    }

    @Test
    void testDel(){
        int i = typeMapper.deleteType((long) 21);
        System.out.println("######################"+i);
    }

//    @Test
//    void testBlog(){
//        List<Blog> allBlog = blogMapper.getAllBlog();
//        System.out.println(allBlog);
//    }

    @Test
    void testTestTags(){
//        Blog blog = tagMapper.getDetailedBlog((long) 5);
        Blog blog = blogMapper.getDetailedBlog((long) 8);
//        System.out.println("==="+blog.getTagIds());
//        blog.setTagIds(blog.tagsToIds(blog.getTags()));
//        System.out.println("<><><>"+blog.getTags());
//        System.out.println("###"+blog.getTagIds());
//        System.out.println(blog);
    }

    @Test
    void testComment(){
        Comment comment = new Comment();
        comment.setNickname("小天");
        comment.setAvatar("/images/avatar.jpg");
        comment.setContent("这是小天的评论");

        Blog blog = new Blog();
        blog.setId((long)27);
        comment.setBlog(blog);

        Comment parentComment = new Comment();
        parentComment.setId((long) -1);
        comment.setParentComment(parentComment);


        commentMapper.saveComment(comment);
    }

    @Test
    void testxxx(){
        Page<Blog> blogs = blogService.selectAllByTypeId((long) 5, 1, 6);
        for (Blog blog : blogs){
            System.out.println("#############"+blog);
        }
    }

}
