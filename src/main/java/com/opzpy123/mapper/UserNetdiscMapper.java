package com.opzpy123.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.opzpy123.model.UserNetdisc;
import com.opzpy123.model.UserWeather;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserNetdiscMapper extends BaseMapper<UserNetdisc> {

    @Select("select count(distinct(user_id)) from user_netdisc;")
    Long getUsersNum();
}
