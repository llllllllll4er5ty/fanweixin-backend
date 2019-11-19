package com.leicx.weixin.mapper;


import com.leicx.weixin.entity.MyFriends;
import com.leicx.weixin.pojo.vo.UsersVO;
import com.leicx.weixin.util.IBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MyFriendsMapper extends IBaseMapper<MyFriends> {
    /**
     * 根据自己的用户id和朋友的用户id获取数据
     * @author daxiong
     * @date 2019-10-28 17:01
     * @param myUserId
     * @param myFriendUserId
     * @return com.leicx.weixin.entity.MyFriends
     **/
    MyFriends getMyFriendsByIds(String myUserId, String myFriendUserId);

    /**
     * 插入好友记录
     * @author daxiong
     * @date 2019-10-30 13:54
     * @param myFriends
     * @return java.lang.Integer
     **/
    Integer insert(@Param("myFriends") MyFriends myFriends);

    /**
     * 获取好友列表
     * @author daxiong
     * @date 2019-10-31 16:29
     * @param userId
     * @return java.util.List<com.leicx.weixin.pojo.vo.UsersVO>
     **/
    List<UsersVO> getMyFriendsList(@Param("userId") String userId);
}