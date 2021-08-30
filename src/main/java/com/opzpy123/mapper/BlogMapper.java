package com.opzpy123.mapper;

import com.opzpy123.bean.Blog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface BlogMapper {

    List<Blog> selectAllBlog();

    Blog selectById(Long id);

}
