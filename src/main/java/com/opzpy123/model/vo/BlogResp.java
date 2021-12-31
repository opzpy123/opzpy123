package com.opzpy123.model.vo;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.opzpy123.model.Blog;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class BlogResp {

    private String id;
    private Integer isDelete;
    private String createTime;
    private String updateTime;
    private String userName;        //发布的用户
    private String content;         //文章内容
    private String about;           //关于
    private String link;            //引用链接
    private String intro;           //介绍
    private String title;           //标题
    private Integer sort;           //排序字段

    public void fromBlog(Blog blog) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.setId(blog.getId() + "");
        this.setCreateTime(sdf.format(blog.getCreateTime()));
        this.setUpdateTime(sdf.format(blog.getUpdateTime()));
        BeanUtils.copyProperties(blog, this);
    }
}
