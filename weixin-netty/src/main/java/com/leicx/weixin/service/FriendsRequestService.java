package com.leicx.weixin.service;

import com.leicx.weixin.entity.FriendsRequest;
import com.leicx.weixin.pojo.vo.UsersVO;

import java.util.List;

/**
 * 好友请求service层
 * @author daxiong
 * @date 2019-10-28 16:51
 **/
public interface FriendsRequestService {
    /**
     * 插入好友请求记录
     * @author daxiong
     * @date 2019-10-29 14:38
     * @param friendsRequest
     * @return void
     **/
    void insert(FriendsRequest friendsRequest);

    /**
     * 根据两个id获取好友请求记录
     * @author daxiong
     * @date 2019-10-29 14:43
     * @param sendUserId
     * @param acceptUserId
     * @return com.leicx.weixin.entity.FriendsRequest
     **/
    FriendsRequest sendFriendsRequest(String sendUserId, String acceptUserId);

    /**
     * 获取好友请求列表
     * @author daxiong
     * @date 2019-10-29 16:29
     * @param acceptUserId
     * @return
     **/
    List<UsersVO> getListBySendUserIdJoinUsers(String acceptUserId);

    /**
     * 处理好友请求，接收或拒绝
     * @author daxiong
     * @date 2019-10-30 11:11
     * @param myUserId
     * @param friendUserId
     * @param operateType   操作类型，0：拒绝；1：接收
     * @return void
     **/
    void handleFriendRequest(String myUserId, String friendUserId, Integer operateType);

    /**
     * 获取好友请求的总数量
     * @author daxiong
     * @date 2019-11-15 13:54
     * @param myUserId
     * @return java.lang.Integer
     */
    Integer getFriendRequestCountBySendUserId(String myUserId);
}
