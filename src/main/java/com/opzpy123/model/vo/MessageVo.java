package com.opzpy123.model.vo;

import com.opzpy123.model.BaseModel;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 用户redis发送消息的vo
 */
@Data
@Slf4j
public class MessageVo{

    private String message;
    private String userName;
    private String sendTime;
}
