package com.leicx.weixin.netty;

import com.leicx.weixin.entity.ChatMsg;
import com.leicx.weixin.enums.MsgTypeEnum;
import com.leicx.weixin.enums.SignFlagEnum;
import com.leicx.weixin.netty.pojo.DataContent;
import com.leicx.weixin.netty.pojo.MsgPojo;
import com.leicx.weixin.service.ChatMsgService;
import com.leicx.weixin.util.JsonUtils;
import com.leicx.weixin.util.SpringUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author daxiong
 * @date 2019-09-27 15:01
 * @since v1.0
 */
public class WSChannelHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static ChannelGroup users = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void messageReceived(ChannelHandlerContext ctx, TextWebSocketFrame textWebSocketFrame) throws Exception {
        // 获取客户端发来的消息
//        String content = textWebSocketFrame.text();
//        System.out.println("接收到的消息：" + content);
//        for (Channel clent : users) {
//            clent.writeAndFlush(new TextWebSocketFrame("[服务器]" + LocalDateTime.now() + "接收消息为：" + content));
//        }
//        users.writeAndFlush(new TextWebSocketFrame("[服务器]" + LocalDateTime.now() + "接收消息为：" + content));

        Channel currentChannel = ctx.channel();

        // 1.获取客户端发来的消息
        String content = textWebSocketFrame.text();
        DataContent dataContent = JsonUtils.jsonToPojo(content, DataContent.class);
        Integer action = dataContent.getAction();
        MsgTypeEnum msgTypeByCode = MsgTypeEnum.getMsgTypeByCode(action);

        // 2.判断消息类型，根据不同的类型来处理不同的业务
        MsgPojo msgPojo = dataContent.getMsgPojo();
        ChatMsgService chatMsgService = null;
        switch (msgTypeByCode) {
            case CONNECT:
                //  2.1 当websocket第一次open的时候，初始化channel，把用户的channel和userid关联起来
                String senderId = msgPojo.getSenderId();
                UserChannelRel.put(senderId, currentChannel);

                // 测试
                users.forEach(u -> System.out.println(u.id().asLongText()));
                UserChannelRel.output();
                break;
            case CHAT:
                //  2.2 聊天类型的消息，把聊天记录保存到数据库，同时标记消息的签收状态[未签收]
                ChatMsg chatMsg = ChatMsg.builder().sendUserId(msgPojo.getSenderId())
                        .acceptUserId(msgPojo.getReceiverId())
                        .msg(msgPojo.getMsg())
                        .signFlag(SignFlagEnum.UNSIGN.getCode())
                        .createTime(new Date())
                        .build();

                // 保存到数据库
                if (null == chatMsgService) {
                    chatMsgService = (ChatMsgService) SpringUtil.getBean("chatMsgServiceImpl");
                }
                chatMsgService.insert(chatMsg);
                msgPojo.setMsgId(chatMsg.getId());
                // 返回给前端的dataContent
                dataContent.setMsgPojo(msgPojo);

                // 消息的发送，获取接收方的channel
                Channel receiveChannel = UserChannelRel.get(msgPojo.getReceiverId());
                if (null == receiveChannel) {
                    // TODO: 2019-11-04 用户离线，推送消息（JPush，个推，小米推送）
                } else {
                    // 从users中查找对应的channel是否存在
                    Channel findChannel = users.find(receiveChannel.id());
                    if (null != findChannel) {
                        // 用户在线
                        receiveChannel.writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(dataContent)));
                    } else {
                        // 用户离线，推送消息
                    }
                }
                break;
            case SIGN:
                //  2.3 签收类型的消息，修改数据库中对应消息的签收状态[已签收]
                // extend字段在消息类型为签收的情况下，代表的是消息id，逗号间隔
                String extend = dataContent.getExtend();
                String[] split = extend.split(",");
                List<String> msgIdList = Arrays.asList(split);
                if (msgIdList.size() > 0) {
                    if (null == chatMsgService) {
                        chatMsgService = (ChatMsgService) SpringUtil.getBean("chatMsgServiceImpl");
                    }
                    chatMsgService.updateSignFlagByIds(msgIdList);
                }
                break;
            case KEEPALIVE:
                //  2.4 心跳类型的消息
                System.out.println("接收到来自channel为[" + currentChannel.id().asShortText() + "]的心跳监测包...");
                break;
            default:
                break;
        }
        
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
         users.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        // 当触发handlerRemoved，ChannelGroup会自动移除相应的客户端
        users.remove(ctx.channel());
        System.out.println("客户端断开，对应channel'长id为：" + ctx.channel().id().asLongText());
        System.out.println("客户端断开，对应channel'短id为：" + ctx.channel().id().asShortText());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        // 发生异常之后，关闭连接channel，然后从ChannelGroup中移除
        ctx.channel().close();
        users.remove(ctx.channel());
    }
}
