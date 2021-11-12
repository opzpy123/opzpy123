package com.opzpy123.controller;

import com.opzpy123.model.Blog;
import com.opzpy123.model.vo.ApiResponse;
import com.opzpy123.service.BlogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
}
