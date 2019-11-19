package com.leicx.weixin.mapper;

import com.leicx.weixin.entity.ChatMsg;
import com.leicx.weixin.util.IBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMsgMapper extends IBaseMapper<ChatMsg> {

    /**
     * 插入聊天消息实体
     * @author daxiong
     * @date 2019-11-04 14:57
     * @param chatMsg
     * @return java.lang.Integer
     **/
    Integer insert(@Param("chatMsg") ChatMsg chatMsg);

    /**
     * 更新消息状态为已签收
     * @author daxiong
     * @date 2019-11-04 15:03
     * @param ids
     * @return void
     **/
    void updateSignFlagByIds(@Param("ids") List<String> ids);

    /**
     * 获取用户未签收的消息
     * @author daxiong
     * @date 2019-11-15 15:20
     * @param acceptUserId    用户id
     * @return java.util.List<com.leicx.weixin.entity.ChatMsg>
     */
    List<ChatMsg> getUnsignedMsgByUserId(@Param("acceptUserId") String acceptUserId);
}