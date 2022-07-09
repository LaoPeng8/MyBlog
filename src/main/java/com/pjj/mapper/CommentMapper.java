package com.pjj.mapper;

import com.pjj.entity.Blog;
import com.pjj.entity.Comment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface CommentMapper {

    List<Comment> listCommentByBlogId(Long blogId);

    List<Comment> listCommentByBlogIdAndParentNull(Long blogId);

    List<Comment> listCommentByBlogIdAndParentNotNull(@Param("blogId") Long blogId, @Param("parentId") Long parentId);

    int saveComment(Comment comment);

    Comment selectParentCommentByParentCommentId(Long id);

}
