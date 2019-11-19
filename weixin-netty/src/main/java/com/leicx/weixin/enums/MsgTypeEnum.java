package com.leicx.weixin.enums;

import lombok.Getter;

/**
 * 消息类型枚举
 * @author daxiong
 * @date 2019-11-04 13:34
 * @return
 **/
@Getter
public enum  MsgTypeEnum {
    // 消息类型枚举
    CONNECT(1, "第一次（或重连）初始化连接"),
    CHAT(2, "聊天消息"),
    SIGN(3, "签收消息"),
    KEEPALIVE(4, "客户端保持心跳"),
    PULL_FRIENDS(5, "重新拉取好友"),


    UNKNOW(404, "未知枚举"),

    ;

    private Integer msgCode;
    private String msgType;

    MsgTypeEnum(Integer msgCode, String msgType) {
        this.msgCode = msgCode;
        this.msgType = msgType;
    }

    // 根据消息码获得消息类型枚举
    public static MsgTypeEnum getMsgTypeByCode(Integer msgCode) {
        for (MsgTypeEnum msgTypeEnum : values()) {
            if (msgTypeEnum.getMsgCode().equals(msgCode)) {
                return msgTypeEnum;
            }
        }
        return UNKNOW;
    }
}
