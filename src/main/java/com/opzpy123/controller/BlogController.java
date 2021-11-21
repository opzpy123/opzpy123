package com.opzpy123.controller;

import com.opzpy123.model.Blog;
import com.opzpy123.model.vo.ApiResponse;
import com.opzpy123.service.BlogService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("blog")
public class BlogController {

    @Resource
    private BlogService blogService;

    @GetMapping
    @ResponseBody
    ApiResponse<List<Blog>> getBlogList() {
        return ApiResponse.ofSuccess(blogService.list());
    }

    @PostMapping
    @ResponseBody
    ApiResponse<String> addBlog(@RequestBody Blog blog) {
        log.info(blog.getUserName()+"创建了blog:"+blog.getTitle());
        return blogService.addBlog(blog);
    }

    @PutMapping
    @ResponseBody
    ApiResponse<String> updateBlog(@RequestBody Blog blog) {
        blogService.updateById(blog);
        log.info(blog.getUserName()+"修改了blog:"+blog.getTitle());
        return ApiResponse.ofSuccess();
    }

    @DeleteMapping
    @ResponseBody
    ApiResponse<String> deleteBlog(@RequestBody Blog blog) {
        blogService.removeById(blog);
        log.info(blog.getUserName()+"删除了blog:"+blog.getTitle());
        return ApiResponse.ofSuccess();
    }
}
