package com.pjj.controller.admin;

import com.github.pagehelper.Page;
import com.pjj.entity.Tag;
import com.pjj.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class TagsController {

    @Autowired
    private TagService tagService;

    @GetMapping("/tags")
    public String types(@RequestParam(value = "id",required = false,defaultValue = "1") Integer whatPage, @RequestParam(required = false,defaultValue = "8") Integer pageMaxData, Model model){
        if(pageMaxData<5){//每页的数据,最少也要有5条吧
            pageMaxData=5;
        }else if (pageMaxData>10){//每页的数据,最多也只能有10条
            pageMaxData=10;
        }
        if(whatPage==0 || whatPage<-1){//分页最起码是第一页,如果请求第0页,或者请求负数页,则重置为第一页
            whatPage=1;
        }
        int pageMax = 1;//一共多少页
        int count = tagService.countTag();//最大条数
        if(count%pageMaxData != 0){//如果最大条数 魔除 每页多少条数据 != 0 则说明 最后一页还是有数据,但是不满 每页的条数
            pageMax = count/pageMaxData;//比如 13/8 = 1余5 那么这个余5的数据也需要一页,所以就+1
            pageMax+=1;//加一页 给最后剩余的数据
        }else if(count%pageMaxData == 0){
            pageMax = count/pageMaxData;//比如 16/8 = 2余0  那么这个余0就不需要再+1页了, 直接就是2就完事了.
        }
        if(whatPage>pageMax){//请求第x页, 不能超过最后一页, 如果超过, 则重置成最后一页
            whatPage = pageMax;
        }
        Page<Tag> page = tagService.getTagWithPagePlus(whatPage, pageMaxData);
//        PageInfo<Type> pageInfo = new PageInfo<>(page);//PageInfo比Page功能更加强大,方法更多

        model.addAttribute("page",page);

        return "admin/tags";
    }

    //跳转到新增页面
    @RequestMapping("/tags/input")
    public String typesInput(){
        return "admin/tags-input";
    }

    //新增页面的提交form,提交到这里
    @PostMapping("/tags/input/insert")
    public String insertType(@RequestParam String name, RedirectAttributes attributes){
        if (name!=null && !name.equals("")){
            int flag = tagService.saveTag(new Tag(name));
            if (flag == 1){
                //新增成功
                attributes.addFlashAttribute("msg","新增成功");
            } else {
                //新增失败
                attributes.addFlashAttribute("msg","新增失败");
            }
        }
        return "redirect:/admin/tags?id=10000";//新增分类完成,跳转到管理分类页面,并且直接跳到最后一页(可以直接看到刚才新增的标签).
    }

    //跳转到修改页面
    @PostMapping("/tags/update/{id}")
    public String editInput(@PathVariable Long id, @RequestParam Integer pageIndex, Model model){
        if(pageIndex==0 || pageIndex==null){
            pageIndex=1;
        }
        if(id!=null && id!=0){
            model.addAttribute("type", tagService.getTag(id));
            model.addAttribute("pageIndex",pageIndex);
        }
        return "admin/tags-update";
    }

    //修改页面的提交form,提交到这里
    @PutMapping("/tags/input/update/{id}")
    public String updateType(@PathVariable Long id, @RequestParam String name, @RequestParam Integer pageIndex, RedirectAttributes attributes){
        if(pageIndex==0 || pageIndex==null){
            pageIndex=1;
        }
        if(id!=null && id!=0){
            if (name!=null && !name.equals("")){
                int flag = tagService.updateTag(new Tag(id,name));
                if (flag == 1){
                    //修改成功
                    attributes.addFlashAttribute("msg","修改成功");
                } else {
                    //修改失败
                    attributes.addFlashAttribute("msg","修改失败");
                }
            }
        }
        //修改分类名称完成,跳转到管理分类页面,并且直接跳到刚才修改页面的那一页(可以直接看到刚才修改的标签).
        return "redirect:/admin/tags?id="+pageIndex;
    }

    @DeleteMapping("/tags/delete/{id}")
    public String delete(@PathVariable Long id,@RequestParam Integer pageIndex,RedirectAttributes attributes){
        if(pageIndex==0 || pageIndex==null){
            pageIndex=1;
        }
        if(id!=null && id!=0){
            int flag = tagService.deleteTag(id);
            if (flag == 1){
                //新增成功
                attributes.addFlashAttribute("msg","删除成功");
            } else {
                //新增失败
                attributes.addFlashAttribute("msg","删除失败");
            }
        }

        //删除分类完成,跳转到管理分类页面,并且直接跳到刚才删除页面的那一页(可以直接看到刚才删除的标签).
        return "redirect:/admin/tags?id="+pageIndex;
    }

}
