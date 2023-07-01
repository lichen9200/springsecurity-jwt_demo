package com.bbbuuuyyy.taobao.entity;

import java.io.Serializable;

//序列化，转为二进制字节流，方便网络传输
public class Post implements Serializable {
    private Integer id;
    private String title;
    private Integer blogId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getBlogId() {
        return blogId;
    }

    public void setBlogId(Integer blogId) {
        this.blogId = blogId;
    }
}
