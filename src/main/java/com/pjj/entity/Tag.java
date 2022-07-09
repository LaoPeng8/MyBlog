package com.pjj.entity;

import java.util.List;

public class Tag {

    private Long id;
    private String name;

    private List<Blog> blogs;//一篇博客有多个标签,一个标签也有多篇博客

    public Tag() {
    }
    public Tag(Long id) {
        this.id = id;
    }
    public Tag(String name) {
        this.name = name;
    }
    public Tag(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Blog> getBlogs() {
        return blogs;
    }

    public void setBlogs(List<Blog> blogs) {
        this.blogs = blogs;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
