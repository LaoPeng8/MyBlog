package com.pjj.controller;

import com.pjj.entity.Type;
import com.pjj.service.BlogService;
import com.pjj.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class TypeShowController {

    @Autowired
    private TypeService typeService;

    @Autowired
    private BlogService blogService;

    @GetMapping("/types/{typeId}")
    public String types(@RequestParam(value = "id",required = false,defaultValue = "1") Integer whatPage,
                        @RequestParam(required = false,defaultValue = "6") Integer pageMaxData,
                        @PathVariable Long typeId, Model model){

        List<Type> allType = typeService.getAllTypeByTop();//查询出全部分类
        if(typeId == -1){//如果跳转到分类页面时没有传递typeId 则默认 为第一个分类的 typeId 然后根据这个typeId查询属于当分类的博客
            typeId = allType.get(0).getId();
        }
        model.addAttribute("types",allType);//查询出所有分类
        model.addAttribute("page",blogService.selectAllByTypeId(typeId,whatPage,pageMaxData));//根据当前分类,查询出属于当前分类的所有博客
        model.addAttribute("activeTypeId",typeId);//将当前选中的分类id传递回去
        return "types";
    }

}
