package com.leicx.weixin.netty.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 消息实体pojo
 * @author daxiong
 * @date 2019-11-04 13:29
 * @return
 **/
@Getter
@Setter
public class MsgPojo implements Serializable {
    private static final long serialVersionUID = -2673856922117499319L;

    // 发送者的用户id
    private String senderId;
    // 接受者的用户id
    private String receiverId;
    // 聊天内容
    private String msg;
    // 消息id，用于消息的签收
    private String msgId;
}
