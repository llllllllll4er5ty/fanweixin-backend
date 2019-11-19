package com.leicx.weixin.netty.pojo;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 前端传入的消息pojo类
 * @author daxiong
 * @date 2019-11-04 13:18
 * @return
 **/
@Getter
@Setter
public class DataContent implements Serializable {
    private static final long serialVersionUID = 2413623110395247573L;
    // 消息类型
    private Integer action;
    // 消息实体
    private MsgPojo msgPojo;
    // 扩展字段
    private String extend;

}
