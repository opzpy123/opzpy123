package com.opzpy123.controller;

import com.opzpy123.model.Blog;
import com.opzpy123.model.vo.ApiResponse;
import com.opzpy123.model.vo.EditorApiResponse;
import com.opzpy123.service.BlogService;
import com.opzpy123.util.OssUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.security.Principal;
import java.util.ArrayList;
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
        log.info(blog.getUserName() + "创建了blog:" + blog.getTitle());
        return blogService.addBlog(blog);
    }

    @PutMapping
    @ResponseBody
    ApiResponse<String> updateBlog(@RequestBody Blog blog) {
        blog.setUserName(null);
        blogService.updateById(blog);
        log.info(blog.getUserName() + "修改了blog:" + blog.getTitle());
        return ApiResponse.ofSuccess();
    }

    @DeleteMapping
    @ResponseBody
    ApiResponse<String> deleteBlog(@RequestBody Blog blog, Principal principal) {
        if (principal.getName().equals("admin")) {
            blogService.removeById(blog);
        } else {
            Blog blogById = blogService.getById(blog.getId());
            if (blogById != null && blogById.getUserName().equals(principal.getName())) {
                blogService.removeById(blog);
            }
        }
        log.info(blog.getUserName() + "删除了blog:" + blog.getTitle());
        return ApiResponse.ofSuccess();
    }

    @SneakyThrows
    @ResponseBody
    @PostMapping("upload")
    @Transactional(rollbackFor = Exception.class)
    EditorApiResponse upload(MultipartFile file, Principal principal) {
        OssUtils ossUtils = new OssUtils();
        String url = ossUtils.upload(file.getInputStream(), "editor/" + principal.getName() + "/"+System.currentTimeMillis() + file.getOriginalFilename());
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
