package com.opzpy123.model;

import com.baomidou.mybatisplus.annotation.*;
import com.opzpy123.constant.enums.UserRolesEnum;
import lombok.Data;



@Data
public class AuthUser extends BaseModel {

    private String username;

    private String password;

    private String avatar;

    private String email;

    private String phone;

    @TableField(fill = FieldFill.INSERT)
    private UserRolesEnum roles;
}
