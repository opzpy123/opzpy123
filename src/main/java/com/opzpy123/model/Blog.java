package com.opzpy123.model;

import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
public class Blog extends BaseModel {

    private Long userId;          //发布的用户
    private String content;         //文章内容
    private String about;           //关于
    private String link;            //引用链接
    private String intro;           //介绍
    private String title;           //标题
}
