package com.pjj.entity;

/**
 * 把博客和标签关系存到数据库中使用的类
 */

public class BlogAndTag {

    private Long tagId;

    private Long blogId;

    public BlogAndTag() {
    }
    public BlogAndTag(Long tagId, Long blogId) {
        this.tagId = tagId;
        this.blogId = blogId;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }

    @Override
    public String toString() {
        return "BlogAndTag{" +
                "tagId=" + tagId +
                ", blogId=" + blogId +
                '}';
    }
}
