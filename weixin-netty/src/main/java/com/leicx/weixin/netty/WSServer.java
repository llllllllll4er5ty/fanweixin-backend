package com.leicx.weixin.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * @author daxiong
 * @date 2019-09-27 13:35
 * @since v1.0
 */
public class WSServer {

    private static WSServer instace = null;

    private EventLoopGroup parentGroup;
    private EventLoopGroup childGroup;
    private ServerBootstrap bootstrap;
    private ChannelFuture future;

    private WSServer() {
        this.parentGroup = new NioEventLoopGroup();
        this.childGroup = new NioEventLoopGroup();
        this.bootstrap = new ServerBootstrap();
        this.bootstrap = new ServerBootstrap();
        bootstrap.group(parentGroup, childGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new WSServerInitializer());
    }

    public static WSServer getInstance() {
        if (instace == null) {
            instace = new WSServer();
        }
        return instace;
    }

    public void start() {
        this.future = this.bootstrap.bind(8088);
        System.out.println("========netty服务启动成功==========");
    }

    private class WSServerInitializer extends ChannelInitializer<SocketChannel> {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
            ChannelPipeline pipeline = socketChannel.pipeline();
            // websocket协议基于http协议，所以要增加http编解码器
            pipeline.addLast(new HttpServerCodec())
                    // 对写大数据流的支持
                    .addLast(new ChunkedWriteHandler())
                    // 对httpMessage进行聚合，聚合成FullHttpRequest或FullHttpResponse
                    // 几乎在netty的所有编程中，都会用到次handler
                    .addLast(new HttpObjectAggregator(1024 * 64))

                    // 进入读写超时的handler监测器，单位：秒
                    .addLast(new IdleStateHandler(8, 10, 12))
                    // 心跳包的检测handler
                    .addLast(new HeartBeatHandler());

            // websocket协议，用于指定客户端连接访问的路由
            // 会帮你处理一些繁重的工作，如握手（handshaking）
            // 对于websocket，都是以frames进行传输的，不同的数据类型对应的frames也不同
            pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));

            // 自定义handler
            pipeline.addLast(new WSChannelHandler());
        }
    }
}
