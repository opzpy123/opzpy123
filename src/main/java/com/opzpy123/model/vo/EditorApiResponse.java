package com.opzpy123.model.vo;

import lombok.Data;

import java.util.List;

@Data
public class EditorApiResponse {
    private Integer errno;
    private List<InnerData> data;

    @Data
    public static class InnerData{
        private String url;
        private String alt;
        private String href;
    }
}
