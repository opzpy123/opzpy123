package com.opzpy123.model;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.sql.Timestamp;


@Data
public class User extends  BaseModel{

    private String name;

    private String password;

    private String avatar;

    private String email;

    private String phone;
}
