package com.opzpy123.controller;

import com.opzpy123.model.Blog;
import com.opzpy123.model.vo.ApiResponse;
import com.opzpy123.model.vo.EditorApiResponse;
import com.opzpy123.service.BlogService;
import com.opzpy123.util.OssUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("blog")
public class BlogController {

    @Resource
    private BlogService blogService;

    @GetMapping
    @ResponseBody
    public ApiResponse<List<Blog>> getBlogList() {
        return blogService.getBlogList();
    }

    @PostMapping
    @ResponseBody
    public ApiResponse<String> addBlog(@RequestBody Blog blog) {
        return blogService.addBlog(blog);
    }

    @PutMapping
    @ResponseBody
    public ApiResponse<String> updateBlog(@RequestBody Blog blog) {
        return blogService.updateBlog(blog);
    }

    @DeleteMapping
    @ResponseBody
    public ApiResponse<String> deleteBlog(@RequestBody Blog blog, Principal principal) {
        return blogService.deleteBlog(blog,principal);
    }

    @SneakyThrows
    @ResponseBody
    @PostMapping("upload")
    @Transactional(rollbackFor = Exception.class)
    public EditorApiResponse upload(MultipartFile file, Principal principal) {
        OssUtils ossUtils = new OssUtils();
        String url = ossUtils.upload(file.getInputStream(), "editor/" + principal.getName() + "/" + System.currentTimeMillis() + file.getOriginalFilename());
        EditorApiResponse editorApiResponse = new EditorApiResponse();
        editorApiResponse.setErrno(0);
        ArrayList<EditorApiResponse.InnerData> innerDataArrayList = new ArrayList<>();
        EditorApiResponse.InnerData innerData = new EditorApiResponse.InnerData();
        innerData.setUrl(url);
        innerData.setHref(url);
        innerData.setAlt(file.getOriginalFilename());
        innerDataArrayList.add(innerData);
        editorApiResponse.setData(innerDataArrayList);
        return editorApiResponse;
    }
}
