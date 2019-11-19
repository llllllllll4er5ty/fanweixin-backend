package com.leicx.weixin.mapper;

import com.leicx.weixin.entity.FriendsRequest;
import com.leicx.weixin.pojo.vo.UsersVO;
import com.leicx.weixin.util.IBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendsRequestMapper extends IBaseMapper<FriendsRequest> {

    /**
     * 插入记录
     * @author daxiong
     * @date 2019-10-29 16:18
     * @param friendsRequest
     * @return java.lang.Integer
     **/
    Integer insert(@Param("friendsRequest") FriendsRequest friendsRequest);

    /**
     * 根据两个userId获取记录
     * @author daxiong
     * @date 2019-10-29 16:18
     * @param sendUserId
     * @param acceptUserId
     * @return com.leicx.weixin.entity.FriendsRequest
     **/
    FriendsRequest getFriendsRequestByIds(@Param("sendUserId") String sendUserId, @Param("acceptUserId") String acceptUserId);

    /**
     * 通过接受者id获取好友请求记录
     * @author daxiong
     * @date 2019-10-29 16:28
     * @param acceptUserId
     * @return java.util.List<com.leicx.weixin.pojo.vo.UsersVO>
     **/
    List<UsersVO> getListBySendUserIdJoinUsers(@Param("acceptUserId") String acceptUserId);

    /**
     * 通过接受者id获取好友请求记录的总数量
     * @author daxiong
     * @date 2019-11-15 13:51
     * @param acceptUserId
     * @return java.lang.Integer
     */
    Integer getFriendRequestCountBySendUser(@Param("acceptUserId") String acceptUserId);

    /**
     * 删除好友请求记录
     * @author daxiong
     * @date 2019-10-30 13:40
     * @param sendUserId
     * @param acceptUserId
     * @return void
     **/
    void deleteFriendRequest(@Param("sendUserId") String sendUserId, @Param("acceptUserId") String acceptUserId);
}