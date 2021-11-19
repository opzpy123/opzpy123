package com.opzpy123.controller;

import com.opzpy123.model.Blog;
import com.opzpy123.model.vo.ApiResponse;
import com.opzpy123.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

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
        System.out.println(blog);
        return blogService.addBlog(blog);
    }

    @PutMapping
    @ResponseBody
    ApiResponse<String> updateBlog(@RequestBody Blog blog) {
        blogService.updateById(blog);
        return ApiResponse.ofSuccess();
    }

    @DeleteMapping
    @ResponseBody
    ApiResponse<String> deleteBlog(@RequestBody Blog blog) {
        blogService.removeById(blog);
        return ApiResponse.ofSuccess();
    }
}
