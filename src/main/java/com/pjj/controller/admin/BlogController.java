package com.pjj.controller.admin;

import com.github.pagehelper.Page;
import com.pjj.entity.Blog;
import com.pjj.entity.Tag;
import com.pjj.entity.Type;
import com.pjj.entity.User;
import com.pjj.service.BlogService;
import com.pjj.service.TagService;
import com.pjj.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private BlogService blogService;
    @Autowired
    private TypeService typeService;
    @Autowired
    private TagService tagService;

    @RequestMapping(value = "/blogs",method = RequestMethod.GET)
    public String blogs(@RequestParam(value = "id",required = false,defaultValue = "1") Integer whatPage,
                        @RequestParam(required = false,defaultValue = "8") Integer pageMaxData,
                        @RequestParam(required = false,defaultValue = "0") String typeId,
                        @RequestParam(required = false,defaultValue = "") String title,
                        @RequestParam(required = false,defaultValue = "false") boolean recommend,
                        Model model){
        Blog blog = new Blog();
        blog.setType(new Type(Long.parseLong(typeId)));
        blog.setTitle(title);
        blog.setRecommend(recommend);
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

        Page<Blog> page = blogService.getBlogWithPageBack(whatPage, pageMaxData, blog);
        model.addAttribute("page",page);
        model.addAttribute("types",typeService.getAllType());

        return "admin/blogs";
    }

    //供前端的 后台页面根据搜索返回结果(前端页面根据ajax请求,后端thymeleaf返回部分页面回去,局部刷新,并没有整个页面刷新)
    @RequestMapping(value = "/blogs/search",method = RequestMethod.POST)
    public String search(@RequestParam(value = "id",required = false,defaultValue = "1") Integer whatPage,
                        @RequestParam(required = false,defaultValue = "8") Integer pageMaxData,
                        @RequestParam(required = false,defaultValue = "0") String typeId,
                        @RequestParam(required = false,defaultValue = "") String title,
                        @RequestParam(required = false,defaultValue = "false") boolean recommend,
                        Model model){
        Blog blog = new Blog();
        blog.setType(new Type(Long.parseLong(typeId)));
        blog.setTitle(title);
        blog.setRecommend(recommend);
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

        Page<Blog> page = blogService.getBlogWithPageBack(whatPage, pageMaxData, blog);
        model.addAttribute("page",page);
        model.addAttribute("types",typeService.getAllType());

        return "admin/blogs :: blogList";
    }

    //跳转到新增表单
    @RequestMapping(value = "/blogs/input")
    public String blogsInput(Model model){

        model.addAttribute("types",typeService.getAllType());
        model.addAttribute("tags",tagService.getAllTag());

        return "admin/blogs-input";
    }

    //新增页面的form表单提交到这
    @PostMapping("/blogs/input/insert")
    public String insertBlog(Blog blog, HttpSession session, RedirectAttributes attributes){
        if(blog!=null){
            //该博客的创建时间, 该博客的更新时间, 博客的浏览次数,这些是前端页面不需要传的,但是我们需要向数据库中写入.(在service层新增一个blog对象的方法中设置了)
            blog.setUserId(((User) session.getAttribute("user")).getId());//用session获取当前登录的用户,那么当前编辑发布的博客,就是属于当前登录用户的.
            blog.setTypeId(blog.getType().getId());//设置博客的分类的id,根据前端传的值
            blog.setTags(tagService.getTagByString(blog.getTagIds()));//设置该博客的,tags
            if(blog.getTitle()!=null && !blog.getTitle().equals("")){
                if(blog.getContent()!=null && !blog.getContent().equals("")){
                    if(blog.getType()!=null && blog.getType().getId()!=null && blog.getType().getId()!=0){
                        int i = blogService.saveBlog(blog);
                        if (i == 1){
                            //新增成功
                            attributes.addFlashAttribute("msg","新增成功");
                        } else {
                            //新增失败
                            attributes.addFlashAttribute("msg","新增失败");
                        }
                    }
                }
            }
        }
        return "redirect:/admin/blogs?id=10000";
    }

    //跳转到修改页面
    @PostMapping("/blogs/update/{id}")
    public String editInput(@PathVariable Long id, @RequestParam Integer pageIndex, Model model){
        if(pageIndex==0 || pageIndex==null){
            pageIndex=1;
        }
        if(id!=null && id!=0){
            //点击编辑时,跳转到编辑页面时根据链接的id,获取到需要编辑的博客的信息,然后展示到页面上
            Blog blog = blogService.getDetailedBlog(id);
            model.addAttribute("blog",blog);
            model.addAttribute("types",typeService.getAllType());
            model.addAttribute("tags",tagService.getAllTag());
            model.addAttribute("pageIndex",pageIndex);

        }
        return "admin/blogs-update";
    }

    @PutMapping("/blogs/input/update/{id}")
    public String updateBlog(@PathVariable Long id, Blog blog, @RequestParam Integer pageIndex, HttpSession session ,RedirectAttributes attributes){
        blog.setUser((User) session.getAttribute("user"));
        blog.setUserId(blog.getUser().getId());
        blog.setType(typeService.getType(blog.getType().getId()));
        blog.setTypeId(blog.getType().getId());
        blog.setTags(tagService.getTagByString(blog.getTagIds()));

        int i = blogService.updateBlog(blog);
        if (i==1){
            attributes.addFlashAttribute("msg","修改成功");
        }else{
            attributes.addFlashAttribute("msg","修改失败");
        }
        return "redirect:/admin/blogs?id="+pageIndex;
    }

    @DeleteMapping("/blogs/delete/{id}")
    public String deleteBlog(@PathVariable Long id,@RequestParam Integer pageIndex,RedirectAttributes attributes){
        if(pageIndex==0 || pageIndex==null){
            pageIndex=1;
        }
        if(id!=null && id!=0){
            int flag = blogService.deleteBlog(id);
            if (flag == 1){
                //新增成功
                attributes.addFlashAttribute("msg","删除成功");
            } else {
                //新增失败
                attributes.addFlashAttribute("msg","删除失败");
            }
        }
        return "redirect:/admin/blogs?id="+pageIndex;
    }

}
