package com.pjj.entity;

import java.util.List;

public class Type {

    private Long id;
    private String name;

    private List<Blog> blogs;//一篇博客只能有一个分类,一个分类含有多篇博客

    public Type() {
    }

    public Type(Long id) {
        this.id = id;
    }

    public Type(String name){
        this.name=name;
    }

    public Type(Long id, String name) {
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
        return "Type{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", blogs=" + blogs +
                '}';
    }
}
