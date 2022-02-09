package com.opzpy123.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.opzpy123.mapper.BlogMapper;
import com.opzpy123.model.Blog;
import com.opzpy123.model.vo.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.List;

@Slf4j
@Service
@CacheConfig(cacheNames = "blog")
public class BlogService extends ServiceImpl<BlogMapper, Blog> {

    @Cacheable
    public ApiResponse<List<Blog>> getBlogList() {
        return ApiResponse.ofSuccess(list());
    }

    @CacheEvict
    public ApiResponse<String> addBlog(Blog blog) {
        baseMapper.insert(blog);
        log.info(blog.getUserName() + "创建了blog:" + blog.getTitle());
        return ApiResponse.ofSuccess();
    }

    @CacheEvict
    public ApiResponse<String> updateBlog(Blog blog) {
        blog.setUserName(null);
        updateById(blog);
        log.info(blog.getUserName() + "修改了blog:" + blog.getTitle());
        return ApiResponse.ofSuccess();
    }

    @CacheEvict
    public ApiResponse<String> deleteBlog(Blog blog, Principal principal) {
        if ("admin".equals(principal.getName())) {
            removeById(blog);
        } else {
            Blog blogById = getById(blog.getId());
            if (blogById != null && blogById.getUserName().equals(principal.getName())) {
                removeById(blog);
            }
        }
        log.info(blog.getUserName() + "删除了blog:" + blog.getTitle());
        return ApiResponse.ofSuccess();
    }
}
