package com.pjj.service;

import com.github.pagehelper.Page;
import com.pjj.entity.Tag;

import java.util.List;

public interface TagService {

    /**
     * 保存(新增一个标签)
     * @param tag
     * @return
     */
    int saveTag(Tag tag);

    /**
     * 根据id查找Tag
     * @param id
     * @return
     */
    Tag getTag(Long id);

    /**
     * 查询全部
     * @return
     */
    List<Tag> getAllTag();

    /**
     * 查询拥有博客数据最多的前多少个标签
     * @return
     */
    List<Tag> getAllTagByTop();

    /**
     * 分页查询 (我承认我取的变量名很辣鸡)
     * 只返回了根据分页查询到的数据
     * @param whatPage 第几页
     * @param pageMaxData 每页多少条数据
     * @return
     */
    List<Tag> getTagWithPage(Integer whatPage,Integer pageMaxData);

    /**
     * 分页查询 (我承认我取的变量名很辣鸡)
     * 直接将page返回,之后可以通过page取出根据分页查询到的数据,还可以获取 当前页、总数据量、总页码、页面大小....等数据
     * @param whatPage 第几页
     * @param pageMaxData 每页多少条数据
     * @return
     */
    Page<Tag> getTagWithPagePlus(Integer whatPage, Integer pageMaxData);

    /**
     * 修改标签
     * @param tag
     * @return
     */
    int updateTag(Tag tag);

    /**
     * 删除标签
     * @param id
     * @return
     */
    int deleteTag(Long id);

    /**
     * 查询有多少个标签
     * @return
     */
    int countTag();

    List<Tag> getTagByString(String text);


}
