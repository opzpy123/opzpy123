package com.opzpy123.controller;

import com.opzpy123.mapper.BlogMapper;
import com.opzpy123.bean.Blog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class BlogRestController {

    @Autowired
    private BlogMapper blogMapper;

    @GetMapping("/blog/{id}")
    public String blog(@PathVariable(name = "id") Long id, Model model){
        List<Blog> blogs = blogMapper.selectAllBlog();
        model.addAttribute("blog",blogs);
        Blog blog = blogMapper.selectById(id);
        model.addAttribute("thisBlog",blog);
        return "Blog";
    }
}
