package com.pjj.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.pjj.entity.Blog;
import com.pjj.entity.Type;
import com.pjj.exception.NotFoundException;
import com.pjj.mapper.TypeMapper;
import com.pjj.service.TypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class TypeServiceImpl implements TypeService {

    @Autowired
    private TypeMapper typeMapper;

    @Transactional
    @Override
    public int saveType(Type type) {
        Type typeByName = typeMapper.getTypeByName(type.getName());
        if(typeByName == null){//说明 新增的分类是 原来的分类中没有的
            return typeMapper.saveType(type);//新增一个分类
        }else{//说明 根据用户想新增的分类名称查询到了一个分类,那么说明原表中已经有了这个分类,就不能新增 名称一样的分类了
            return 0;
        }
    }

    @Override
    public Type getType(Long id) {
        return typeMapper.getType(id);
    }

    @Override
    public List<Type> getAllType() {
        return typeMapper.getAllType();
    }

    @Override
    public List<Type> getAllTypeByTop() {
        //尝试了冒泡排序但是排不好...
        return typeMapper.getAllTypeByTop();
    }

    @Override
    public List<Type> getTypeWithPage(Integer whatPage, Integer pageMaxData) {
        //加入分页功能 jdk8 lambda用法
        Page<Type> page = PageHelper.startPage(whatPage, pageMaxData).doSelectPage(()-> typeMapper.getAllType());
        List<Type> result = page.getResult();
        return result;//只返回了根据分页查询到的数据
    }

    @Override
    public Page<Type> getTypeWithPagePlus(Integer whatPage, Integer pageMaxData) {
        //加入分页功能  这是实现的方式二  jdk8 lambda用法
        Page<Type> page = PageHelper.startPage(whatPage, pageMaxData).doSelectPage(()-> typeMapper.getAllType());
        return page;//直接将page返回,之后可以通过page取出根据分页查询到的数据,还可以获取 当前页、总数据量、总页码、页面大小....等数据
    }

    @Transactional
    @Override
    public int updateType(Type type) {
        Type tempType = typeMapper.getType(type.getId());//根据id看能不能查到分类
        if (tempType == null){
            throw new NotFoundException("不存在该类型");
        }else{
            return typeMapper.updateType(type);//上面判断了既然存在该类型,那么update必然是可以成功的
        }

    }

    @Override
    public int deleteType(Long id) {
        int flag = 0;
        if(id!=0 && id!=null){
            flag = typeMapper.deleteType(id);
        }
        return flag;
    }

    @Override
    public int countType() {
        return typeMapper.countType();
    }
}
