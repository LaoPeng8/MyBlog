package com.pjj.controller;

import com.github.pagehelper.Page;
import com.pjj.entity.Blog;
import com.pjj.entity.Tag;
import com.pjj.entity.Type;
import com.pjj.exception.NotFoundException;
import com.pjj.service.BlogService;
import com.pjj.service.CommentService;
import com.pjj.service.TagService;
import com.pjj.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private BlogService blogService;

    @Autowired
    private TypeService typeService;

    @Autowired
    private TagService tagService;

    @Autowired
    private CommentService commentService;

    @RequestMapping("/")
    public String index(@RequestParam(value = "id",required = false,defaultValue = "1") Integer whatPage,
                        @RequestParam(required = false,defaultValue = "6") Integer pageMaxData,
                        Model model){

        if(pageMaxData<5){//每页的数据,最少也要有5条吧
            pageMaxData=5;
        }else if (pageMaxData>10){//每页的数据,最多也只能有10条
            pageMaxData=10;
        }
        if(whatPage==0 || whatPage<-1){//分页最起码是第一页,如果请求第0页,或者请求负数页,则重置为第一页
            whatPage=1;
        }
        int pageMax = 1;//一共多少页
        int count = blogService.countBlog();//最大条数
        if(count%pageMaxData != 0){//如果最大条数 魔除 每页多少条数据 != 0 则说明 最后一页还是有数据,但是不满 每页的条数
            pageMax = count/pageMaxData;//比如 13/8 = 1余5 那么这个余5的数据也需要一页,所以就+1
            pageMax+=1;//加一页 给最后剩余的数据
        }else if(count%pageMaxData == 0){
            pageMax = count/pageMaxData;//比如 16/8 = 2余0  那么这个余0就不需要再+1页了, 直接就是2就完事了.
        }
        if(whatPage>pageMax){//请求第x页, 不能超过最后一页, 如果超过, 则重置成最后一页
            whatPage = pageMax;
        }

        Page<Blog> blogs = blogService.getBlogWithPageFront(whatPage, pageMaxData);//首页展示的博客列表,列表上的博客只有部分信息
        List<Type> types = typeService.getAllTypeByTop();//首页展示的博客分类 6 个
        List<Tag> tags = tagService.getAllTagByTop();//首页展示的博客标签 N个
        List<Blog> blogTitles = blogService.selectAllByTop();//首页展示的推荐博客的 前6个    只有标题和id

        model.addAttribute("blogs",blogs);
        model.addAttribute("types",types);
        model.addAttribute("tags",tags);
        model.addAttribute("blogTitles",blogTitles);

        return "index";
    }

    @RequestMapping("/search")
    public String search(@RequestParam(required = false,defaultValue = "") String query ,
                         @RequestParam(value = "id",required = false,defaultValue = "1") Integer whatPage,
                         @RequestParam(required = false,defaultValue = "6") Integer pageMaxData,
                         Model model){


        if(pageMaxData<5){//每页的数据,最少也要有5条吧
            pageMaxData=5;
        }else if (pageMaxData>10){//每页的数据,最多也只能有10条
            pageMaxData=10;
        }
        if(whatPage==0 || whatPage<-1){//分页最起码是第一页,如果请求第0页,或者请求负数页,则重置为第一页
            whatPage=1;
        }
        int pageMax = 1;//一共多少页
        int count = blogService.countBlog();//最大条数
        if(count%pageMaxData != 0){//如果最大条数 魔除 每页多少条数据 != 0 则说明 最后一页还是有数据,但是不满 每页的条数
            pageMax = count/pageMaxData;//比如 13/8 = 1余5 那么这个余5的数据也需要一页,所以就+1
            pageMax+=1;//加一页 给最后剩余的数据
        }else if(count%pageMaxData == 0){
            pageMax = count/pageMaxData;//比如 16/8 = 2余0  那么这个余0就不需要再+1页了, 直接就是2就完事了.
        }
        if(whatPage>pageMax){//请求第x页, 不能超过最后一页, 如果超过, 则重置成最后一页
            whatPage = pageMax;
        }

        //动态SQL好像没有效果, 那么我就在这里直接判断query,如果为空,那么查询就是 啥也查不到
        if(query!=null && !query.equals("")){
            Page<Blog> blogs = blogService.selectBySearch(whatPage, pageMaxData, query);
            model.addAttribute("blogs",blogs);
            model.addAttribute("query",query);
            return "search";
        }else{
            //给个对象到前端,要不然thymeleaf取不到值会报错
            Page<Blog> blogs = new Page<>();
            model.addAttribute("blogs",blogs);
            model.addAttribute("query",query);
            model.addAttribute("msg","很遗憾,没有搜索到任何值...");
            return "search";
        }

    }

    @RequestMapping("/blog/{id}")
    public String blog(@PathVariable("id") Long id,Model model){
        model.addAttribute("blog",blogService.getDetailedBlogPlus(id));
        return "blog";
    }

    @RequestMapping("/error/withoutPermission")
    public String error(){
        return "error/withoutPermission";
    }


}
