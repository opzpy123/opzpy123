package com.opzpy123.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opzpy123.mapper.BlogMapper;
import com.opzpy123.model.Blog;
import com.opzpy123.model.vo.ApiResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class BlogService extends ServiceImpl<BlogMapper, Blog> {

    public ApiResponse<String> addBlog(Blog blog) {
        baseMapper.insert(blog);
        return ApiResponse.ofSuccess();
    }
}
