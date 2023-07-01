package com.bbbuuuyyy.taobao.dao;

import com.bbbuuuyyy.taobao.entity.Author;
import com.bbbuuuyyy.taobao.entity.Blog;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface AuthorMapper {
    Integer addAuthor(Author author);

    Author selectAuthorByUsername(String username);
    Blog selectAuthorOwnedBlog(Integer id);

    Author selectAuthorAndItsBlog(Integer id);

}
