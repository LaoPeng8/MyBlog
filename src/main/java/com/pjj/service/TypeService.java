package com.pjj.service;

import com.github.pagehelper.Page;
import com.pjj.entity.Type;

import java.util.List;

public interface TypeService {

    /**
     * 保存(新增一个分类)
     * @param type
     * @return
     */
    int saveType(Type type);

    /**
     * 根据id查找Type
     * @param id
     * @return
     */
    Type getType(Long id);

    /**
     * 查询全部
     * @return
     */
    List<Type> getAllType();

    /**
     * 查询拥有博客数据最多的前多少个分类
     * @return
     */
    List<Type> getAllTypeByTop();

    /**
     * 分页查询 (我承认我取的变量名很辣鸡)
     * 只返回了根据分页查询到的数据
     * @param whatPage 第几页
     * @param pageMaxData 每页多少条数据
     * @return
     */
    List<Type> getTypeWithPage(Integer whatPage,Integer pageMaxData);

    /**
     * 分页查询 (我承认我取的变量名很辣鸡)
     * 直接将page返回,之后可以通过page取出根据分页查询到的数据,还可以获取 当前页、总数据量、总页码、页面大小....等数据
     * @param whatPage 第几页
     * @param pageMaxData 每页多少条数据
     * @return
     */
    Page<Type> getTypeWithPagePlus(Integer whatPage, Integer pageMaxData);
    /**
     * 修改分类
     * @param type
     * @return
     */
    int updateType(Type type);

    /**
     * 删除分类
     * @param id
     * @return
     */
    int deleteType(Long id);

    /**
     * 查询有多少个分类
     * @return
     */
    int countType();

}
