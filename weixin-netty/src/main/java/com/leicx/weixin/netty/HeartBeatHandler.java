package com.leicx.weixin.netty;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;

import java.util.Objects;

/**
 * 处理前端心跳的handler
 * @author daxiong
 * @date 2019-09-27 15:01
 * @since v1.0
 */
public class HeartBeatHandler extends ChannelHandlerAdapter {
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent event = (IdleStateEvent) evt;
            IdleState state = event.state();
            if (Objects.equals(state, IdleState.READER_IDLE)) {
                System.out.println("进入读空闲");
            } else if (Objects.equals(state, IdleState.WRITER_IDLE)) {
                System.out.println("进入写空闲");
            } else if (Objects.equals(state, IdleState.ALL_IDLE)) {
                System.out.println("断开之前的channel数量" + WSChannelHandler.users.size());
                // 断开连接
                ctx.channel().close();
                System.out.println("断开之后的channel数量" + WSChannelHandler.users.size());
            }
        }
    }
}