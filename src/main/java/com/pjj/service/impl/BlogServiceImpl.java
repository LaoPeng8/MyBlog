package com.pjj.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pjj.entity.Blog;
import com.pjj.entity.BlogAndTag;
import com.pjj.entity.Tag;
import com.pjj.entity.Type;
import com.pjj.exception.NotFoundException;
import com.pjj.mapper.BlogMapper;
import com.pjj.service.BlogService;
import com.pjj.uitl.MarkdownUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class BlogServiceImpl implements BlogService {

    @Autowired
    private BlogMapper blogMapper;

    @Override
    public Blog getBlog(Long id) {
        return blogMapper.getBlog(id);
    }

    @Override
    public Blog getDetailedBlog(Long id) {
        return blogMapper.getDetailedBlog(id);
    }

    @Override
    public Blog getDetailedBlogPlus(Long id) {
        Blog blog = blogMapper.getDetailedBlog(id);
        if (blog == null){
            throw new NotFoundException("博客不存在");
        }
        String content = blog.getContent();//获取字符串形式的Markdown语法的博客内容
        String contentHtml = MarkdownUtils.markdownToHtmlExtensions(content);//将字符串形式的Markdown语法的博客内容 转为 HTML形式的博客内容
        blog.setContent(contentHtml);//将HTML形式的博客内容赋值给blog

        blogMapper.updateViews(id);

        return blog;

    }

    @Override
    public Page<Blog> getBlogWithPageBack(Integer whatPage, Integer pageMaxData, Blog blog) {
        //加入分页功能  这是实现的方式二  jdk8 lambda用法
        Page<Blog> page = PageHelper.startPage(whatPage, pageMaxData).doSelectPage(()-> blogMapper.getAllBlog(blog.getTitle(),blog.getType().getId(),blog.isRecommend()));
        return page;//直接将page返回,之后可以通过page取出根据分页查询到的数据,还可以获取 当前页、总数据量、总页码、页面大小....等数据
    }

    @Override
    public Page<Blog> selectBySearch(Integer whatPage, Integer pageMaxData, String query) {
        Page<Blog> page = PageHelper.startPage(whatPage, pageMaxData).doSelectPage(()-> blogMapper.selectBySearch(query));
        return page;
    }

    @Override
    public List<Blog> selectAllByTop() {
        return blogMapper.selectAllByTop();
    }

    @Override
    public Page<Blog> getBlogWithPageFront(Integer whatPage, Integer pageMaxData) {
        //加入分页功能  这是实现的方式二  jdk8 lambda用法
        Page<Blog> page = PageHelper.startPage(whatPage, pageMaxData).doSelectPage(()-> blogMapper.selectAllBlog());
        return page;//直接将page返回,之后可以通过page取出根据分页查询到的数据,还可以获取 当前页、总数据量、总页码、页面大小....等数据
    }

    @Override
    public Page<Blog> selectAllByTypeId(Long typeId, Integer whatPage, Integer pageMaxData) {
        Page<Blog> page = PageHelper.startPage(whatPage, pageMaxData).doSelectPage(()-> blogMapper.selectAllByTypeId(typeId));
        return page;
    }

    @Override
    public Page<Blog> selectAllByTagId(Long tagId, Integer whatPage, Integer pageMaxData) {
        Page<Blog> page = PageHelper.startPage(whatPage, pageMaxData).doSelectPage(()-> blogMapper.selectAllByTagId(tagId));
        return page;
    }

    @Override
    public Map<String, List<Blog>> archiveBlog() {

        List<String> years = blogMapper.selectGroupYear();
//        Map<String,List<Blog>> map = new HashMap<>();     hashmap是无序的, 不会按照我们add进容器的顺序取出来
        Map<String, List<Blog>> map = new TreeMap<String, List<Blog>>(      //TreeMap默认是升序的, 也可以在new的时候指定是 升序还是降序
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        // 降序排序
                        return obj2.compareTo(obj1);
                    }
                });

        for(String year:years){
            map.put(year,blogMapper.selectAllByYear(year));
        }
        return map;
    }

    @Transactional
    @Override
    public int saveBlog(Blog blog) {
        //保存一个博客时, 该博客的创建时间, 该博客的更新时间, 博客的浏览次数,这些是前端页面不需要传的,但是我们需要向数据库中写入.
        blog.setCreateTime(new Date());
        blog.setUpdateTime(new Date());
        blog.setViews(100);//自带一百浏览次数,嘿嘿嘿
        //该博客属于哪个用户在Controller层通过 session获取到登录的用户后 设置blog.setUser((User) session.getAttribute("user"));

        int i = blogMapper.saveBlog(blog);

        //保存博客后才能获取自增的id
        Long id = blog.getId();
        //将标签的数据存到t_blogs_tag表中
        List<Tag> tags = blog.getTags();
        BlogAndTag blogAndTag = null;
        for (Tag tag : tags) {
            //新增时无法获取自增的id,在mybatis里修改
            blogAndTag = new BlogAndTag(tag.getId(), id);
            blogMapper.saveBlogAndTag(blogAndTag);
        }
        return i;
    }

    @Override
    public int updateBlog(Blog blog) {
        Blog tempBlog = blogMapper.getBlog(blog.getId());//根据id看能不能查到博客
        if(tempBlog == null){
            throw new NotFoundException("不存在该博客");//所以不能修改
        }else{
            blog.setUpdateTime(new Date());
            //将标签的数据存到t_blogs_tag表中
            List<Tag> tags = blog.getTags();
            BlogAndTag blogAndTag = null;
            for (Tag tag : tags) {
                //新增时无法获取自增的id,在mybatis里修改
                blogAndTag = new BlogAndTag(tag.getId(), blog.getId());
                blogMapper.saveBlogAndTag(blogAndTag);
            }
            return blogMapper.updateBlog(blog);
        }
    }

    @Override
    public int deleteBlog(Long id) {
        Blog blog = blogMapper.getBlog(id);//删除之前先查询
        if(blog==null){//如果没有查询到,则自然删除不了,返回0删除失败
            return 0;
        }else{//如果查询到了,则进行删除操作
            return blogMapper.deleteBlog(id);
        }
    }

    @Override
    public int countBlog() {
        return blogMapper.countBlog();
    }
}
