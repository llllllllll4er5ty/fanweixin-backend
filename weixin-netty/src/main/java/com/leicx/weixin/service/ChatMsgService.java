package com.leicx.weixin.service;

import com.leicx.weixin.entity.ChatMsg;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 聊天消息service层
 * @author daxiong
 * @date 2019-11-04 14:10
 **/
public interface ChatMsgService {
    /**
     * 插入聊天消息记录
     * @author daxiong
     * @date 2019-11-04 14:10
     * @param chatMsg
     * @return Integer
     **/
    Integer insert(ChatMsg chatMsg);

    /**
     * 根据id修改消息签收标志
     * @author daxiong
     * @date 2019-11-04 14:51
     * @param ids
     * @return void
     **/
    void updateSignFlagByIds(List<String> ids);

    /**
     * 获取用户未签收的消息
     * @author daxiong
     * @date 2019-11-15 15:18
     * @param userId    用户id
     * @return java.util.List<com.leicx.weixin.entity.ChatMsg>
     */
    List<ChatMsg> getUnsignedMsgByUserId(String userId);
}
