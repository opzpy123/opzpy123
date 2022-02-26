package com.opzpy123.model.vo;

import com.opzpy123.model.Blog;
import lombok.Data;

import java.util.List;

@Data
public class SearchVo {
    private List<Blog> blogIdList;

    //todo 拓展其他检索条目
}
