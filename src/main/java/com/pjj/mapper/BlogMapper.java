package com.pjj.mapper;

import com.pjj.entity.Blog;
import com.pjj.entity.BlogAndTag;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface BlogMapper {

    Blog getBlog(Long id);

    Blog getDetailedBlog(Long id);//博客详情

    //查询全部, 需要传入条件,动态SQL, 提供给分页使用
    List<Blog> getAllBlog(@Param("title") String title, @Param("typeId") Long typeId, @Param("recommend") boolean recommend);

    List<Blog> selectAllBlog(); //前端的查询全部,提供分页使用

    List<Blog> selectBySearch(String query); //根据前端搜索框输入的值 查询标题或者描述包含 xx值 的博客

    List<Blog> selectAllByTop();//查询出前6条推荐的博客标题

    List<Blog> selectAllByTypeId(Long typeId);//根据分类id查询出全部博客

    List<Blog> selectAllByTagId(Long tagId);//根据标签id查询出全部博客

    List<String> selectGroupYear();//查询所有博客 按照年份分类 并最后返回 年份

    List<Blog> selectAllByYear(String year);//查询所有博客, 根据年份查询

    int updateViews(Long id);//访问一次博客详情, 该博客的浏览次数views就+1

    int saveBlog(Blog blog);

    int saveBlogAndTag(BlogAndTag blogAndTag);

    int updateBlog(Blog blog);

    int deleteBlog(Long id);

    int countBlog();

}