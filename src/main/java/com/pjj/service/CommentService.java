package com.pjj.service;

import com.pjj.entity.Blog;
import com.pjj.entity.Comment;

import java.util.List;

public interface CommentService {

    List<Comment> listCommentByBlogId(Long blogId);

    int saveComment(Comment comment);

    Comment selectParentCommentByParentCommentId(Long id);

}
