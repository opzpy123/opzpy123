package com.opzpy123.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opzpy123.model.AuthUser;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AuthUserMapper extends BaseMapper<AuthUser> {
}
