package com.pjj.service;

import com.github.pagehelper.Page;
import com.pjj.entity.Blog;

import java.util.List;
import java.util.Map;

public interface BlogService {

    /**
     * 根据id查询一篇博客
     * @param id
     * @return
     */
    Blog getBlog(Long id);

    /**
     * 后台博客详情(修改博客时 需要用到blog,type,tag)
     * @param id
     * @return
     */
    Blog getDetailedBlog(Long id);//博客详情

    /**
     * 前台博客详情, 将blog中的content由字符串的markdown语法转为对应HTML标签, 方便在前端页面中展示
     * @return
     */
    Blog getDetailedBlogPlus(Long id);

    /**
     * 分页查询  动态SQL 需要选择是否根据 分类 或 标签 查询
     * 后端版  就是说 数据不全 只有后台展示所需要的部分数据
     * @param whatPage
     * @param pageMaxData
     * @param blog
     * @return
     */
    Page<Blog> getBlogWithPageBack(Integer whatPage, Integer pageMaxData, Blog blog);

    /**
     * 根据前端搜索框输入的值 查询标题或者描述包含 xx值 的博客       分页
     * @param query
     * @return
     */
    Page<Blog>selectBySearch(Integer whatPage, Integer pageMaxData, String query);

    /**
     * 查询出前6条推荐的博客标题
     * @return
     */
    List<Blog> selectAllByTop();

    /**
     * 前台展示的分页查询博客
     * 数据不全,只有前台展示所需要的部分数据
     * @param whatPage
     * @param pageMaxData
     * @return
     */
    Page<Blog> getBlogWithPageFront(Integer whatPage, Integer pageMaxData);

    /**
     * 根据分类id查询出全部博客
     * @param typeId
     * @param whatPage
     * @param pageMaxData
     * @return
     */
    Page<Blog> selectAllByTypeId(Long typeId,Integer whatPage,Integer pageMaxData);

    /**
     * 根据标签id查询出全部博客
     * @param tagId
     * @return
     */
    Page<Blog> selectAllByTagId(Long tagId,Integer whatPage,Integer pageMaxData);

    /**
     * 归档页  按年份展示博客
     * @return
     */
    Map<String,List<Blog>> archiveBlog();

    /**
     * 新增一篇博客
     * @param blog
     * @return
     */
    int saveBlog(Blog blog);

    /**
     * 根据 id 修改一篇博客
     * @param blog
     * @return
     */
    int updateBlog(Blog blog);

    /**
     * 根据 id 删除一篇博客
     * @param id
     * @return
     */
    int deleteBlog(Long id);

    /**
     * 查询一共多少篇博客
     * @return
     */
    int countBlog();
}
