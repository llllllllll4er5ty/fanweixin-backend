package com.leicx.weixin.enums;

import lombok.Getter;

/**
 * 消息签收类型枚举
 * @author daxiong
 * @date 2019-11-04 13:34
 * @return
 **/
@Getter
public enum SignFlagEnum {
    // 消息签收类型枚举
    UNSIGN(0, "未签约"),
    SIGNED(1, "已签约"),


    UNKNOW(404, "未知枚举"),

    ;

    private Integer code;
    private String msg;

    SignFlagEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }}
