package com.leicx.weixin.enums;

import lombok.Getter;

/**
 * 操作类型枚举类型枚举
 * @author daxiong
 * @date 2019-11-04 13:34
 * @return
 **/
@Getter
public enum OperateTypeEnum {
    // 消息签收类型枚举
    REFUSE(0, "拒绝"),
    ACCEPT(1, "同意"),


    UNKNOW(404, "未知枚举"),

    ;

    private Integer code;
    private String msg;

    OperateTypeEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }}
