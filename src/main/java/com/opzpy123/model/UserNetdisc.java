package com.opzpy123.model;

import lombok.Data;

@Data
public class UserNetdisc extends BaseModel{

    private Long userId;

    private String fileName;

    private String url;

    private String path;

    private String size;

}
