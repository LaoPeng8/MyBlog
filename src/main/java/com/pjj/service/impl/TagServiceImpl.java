package com.pjj.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pjj.entity.Tag;
import com.pjj.entity.Type;
import com.pjj.exception.NotFoundException;
import com.pjj.mapper.TagMapper;
import com.pjj.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {

    @Autowired
    private TagMapper tagMapper;

    @Transactional
    @Override
    public int saveTag(Tag tag) {
        Tag tagByName = tagMapper.getTagByName(tag.getName());
        if(tagByName == null){//说明 新增的标签是 原来的标签中没有的
            return tagMapper.saveTag(tag);//新增一个标签
        }else{//说明 根据用户想新增的标签名称查询到了一个标签,那么说明原表中已经有了这个标签,就不能新增 名称一样的标签了
            return 0;
        }
    }

    @Override
    public Tag getTag(Long id) {
        return tagMapper.getTag(id);
    }

    @Override
    public List<Tag> getAllTag() {
        return tagMapper.getAllTag();
    }

    @Override
    public List<Tag> getAllTagByTop() {
        return tagMapper.getAllTagByTop();
    }

    @Override
    public List<Tag> getTagWithPage(Integer whatPage, Integer pageMaxData) {
        //加入分页功能 jdk8 lambda用法
        Page<Tag> page = PageHelper.startPage(whatPage, pageMaxData).doSelectPage(() -> tagMapper.getAllTag());
        List<Tag> result = page.getResult();
        return result;//只返回了根据分页查询到的数据
    }

    @Override
    public Page<Tag> getTagWithPagePlus(Integer whatPage, Integer pageMaxData) {
        //加入分页功能 jdk8 lambda用法
        Page<Tag> page = PageHelper.startPage(whatPage, pageMaxData).doSelectPage(() -> tagMapper.getAllTag());
        return page;//直接将page返回,之后可以通过page取出根据分页查询到的数据,还可以获取 当前页、总数据量、总页码、页面大小....等数据
    }

    @Transactional
    @Override
    public int updateTag(Tag tag) {
        Tag tempTag = tagMapper.getTag(tag.getId());//根据id看能不能查到标签
        if (tempTag == null){//如果查出来==null就说明没有查询到,那么既然原本就没有这个标签,那么怎么修改?
            throw new NotFoundException("不存在该标签");
        }else{
            return tagMapper.updateTag(tag);//上面判断了既然存在该类型,那么update必然是可以成功的
        }
    }

    @Override
    public int deleteTag(Long id) {
        int flag = 0;
        if(id!=0 && id!=null){
            flag = tagMapper.deleteTag(id);
        }
        return flag;
    }

    @Override
    public int countTag() {
        return tagMapper.countTag();
    }

    @Override
    public List<Tag> getTagByString(String text) {    //从tagIds字符串中获取id，根据id获取tag集合
        List<Tag> tags = new ArrayList<>();
        List<Long> longs = convertToList(text);
        for (Long long1 : longs) {
            tags.add(tagMapper.getTag(long1));
        }
        return tags;
    }

    private List<Long> convertToList(String ids) {  //把前端的tagIds字符串转换为list集合
        List<Long> list = new ArrayList<>();
        if (!"".equals(ids) && ids != null) {
            String[] idarray = ids.split(",");
            for (int i=0; i < idarray.length;i++) {
                list.add(new Long(idarray[i]));
            }
        }
        return list;
    }
}
