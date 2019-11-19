package com.leicx.weixin.service;

import com.leicx.weixin.entity.MyFriends;
import com.leicx.weixin.pojo.vo.UsersVO;

import java.util.List;

/**
 * 我的好友service层
 * @author daxiong
 * @date 2019-10-28 16:51
 **/
public interface MyFriendsService {

    /**
     * 根据两个用户id获取好友记录
     * @author daxiong
     * @date 2019-10-30 13:50
     * @param myUserId
     * @param MyFriendUserId
     * @return com.leicx.weixin.entity.MyFriends
     **/
    MyFriends getMyFriendsByIds(String myUserId, String MyFriendUserId);

    /**
     * 插入好友记录
     * @author daxiong
     * @date 2019-10-30 13:51
     * @param myFriends
     * @return java.lang.Integer
     **/
    Integer insert(MyFriends myFriends);

    /**
     * 插入好友记录
     * @author daxiong
     * @date 2019-10-30 13:51
     * @param myUserId
     * @param friendUserId
     * @return java.lang.Integer
     **/
    Integer insert(String myUserId, String friendUserId);

    /**
     * 获取用户的好友列表
     * @author daxiong
     * @date 2019-10-31 16:27
     * @param userId
     * @return java.util.List<com.leicx.weixin.pojo.vo.UsersVO>
     **/
    List<UsersVO> getMyFriendsList(String userId);
}
