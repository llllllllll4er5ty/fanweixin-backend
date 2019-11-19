package com.leicx.weixin.service.impl;

import com.leicx.weixin.entity.MyFriends;
import com.leicx.weixin.mapper.MyFriendsMapper;
import com.leicx.weixin.pojo.vo.UsersVO;
import com.leicx.weixin.service.MyFriendsService;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MyFriendsServiceImpl implements MyFriendsService {

    @Autowired
    private MyFriendsMapper myFriendsMapper;
    @Autowired
    private Sid sid;

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public MyFriends getMyFriendsByIds(String myUserId, String MyFriendUserId) {
        return myFriendsMapper.getMyFriendsByIds(myUserId, MyFriendUserId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    @Override
    public Integer insert(MyFriends myFriends) {
        return myFriendsMapper.insert(myFriends);
    }

    @Override
    public List<UsersVO> getMyFriendsList(String userId) {
        return myFriendsMapper.getMyFriendsList(userId);
    }

    @Override
    public Integer insert(String myUserId, String friendUserId) {
        MyFriends myFriends = new MyFriends();
        myFriends.setId(sid.nextShort());
        myFriends.setMyUserId(myUserId);
        myFriends.setMyFriendUserId(friendUserId);
        return insert(myFriends);
    }
}
