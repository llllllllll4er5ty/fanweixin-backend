package com.leicx.weixin.netty;

import io.netty.channel.Channel;

import java.util.HashMap;

/**
 * 用户id和channelid关系
 * @author daxiong
 * @date 2019-11-04 14:00
 * @return
 **/
public class UserChannelRel {
    private static HashMap<String, Channel> manager = new HashMap<>(1 << 4);

    public static void put(String senderId, Channel channel) {
        manager.put(senderId, channel);
    }

    public static Channel get(String senderId) {
        return manager.get(senderId);
    }

    public static void output() {
        manager.entrySet().forEach(m -> System.out.println(
                "userId:" + m.getKey() + ", channelId:" + m.getValue().id().asLongText()));
    }
}
