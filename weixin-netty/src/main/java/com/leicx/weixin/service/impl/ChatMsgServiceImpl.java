package com.leicx.weixin.service.impl;

import com.leicx.weixin.entity.ChatMsg;
import com.leicx.weixin.mapper.ChatMsgMapper;
import com.leicx.weixin.service.ChatMsgService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ChatMsgServiceImpl implements ChatMsgService {

    @Autowired
    private ChatMsgMapper chatMsgMapper;
    @Autowired
    private Sid sid;

    @Override
    public void updateSignFlagByIds(List<String> ids) {
        chatMsgMapper.updateSignFlagByIds(ids);
    }

    @Override
    public List<ChatMsg> getUnsignedMsgByUserId(String userId) {
        return chatMsgMapper.getUnsignedMsgByUserId(userId);
    }

    @Override
    public Integer insert(ChatMsg chatMsg) {
        chatMsg.setId(sid.nextShort());
        return chatMsgMapper.insert(chatMsg);
    }
}
