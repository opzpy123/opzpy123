package com.opzpy123.controller;

import com.opzpy123.mapper.BlogMapper;
import com.opzpy123.bean.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class indexController {

    @Autowired
    private BlogMapper blogMapper;

    @GetMapping("/")
    public String index(Model model){
        List<Blog> blogs = blogMapper.selectAllBlog();
        model.addAttribute("blog",blogs);
        return "index";
    }



}
