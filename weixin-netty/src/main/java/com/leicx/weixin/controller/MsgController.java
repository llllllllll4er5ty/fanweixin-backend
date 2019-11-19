package com.leicx.weixin.controller;

import com.leicx.weixin.entity.ChatMsg;
import com.leicx.weixin.enums.ErrorCodeEnum;
import com.leicx.weixin.service.ChatMsgService;
import com.leicx.weixin.util.LcxJSONResult;
import com.leicx.weixin.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 处理消息的controller
 * @author daxiong
 * @date 2019-11-1515:10
 */
@RestController
@RequestMapping("/msg")
public class MsgController {

    @Autowired
    private ChatMsgService chatMsgService;

    @GetMapping("getUnsignedMsg")
    public LcxJSONResult getUnsignedMsg(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return LcxJSONResult.errorException(ErrorCodeEnum.ERROR_CODE_100002);
        }

        List<ChatMsg> chatMsgList = chatMsgService.getUnsignedMsgByUserId(userId);
        return LcxJSONResult.ok(chatMsgList);
    }
}
