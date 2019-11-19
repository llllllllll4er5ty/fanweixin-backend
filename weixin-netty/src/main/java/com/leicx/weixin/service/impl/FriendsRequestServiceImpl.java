package com.leicx.weixin.service.impl;

import com.leicx.weixin.entity.FriendsRequest;
import com.leicx.weixin.enums.MsgTypeEnum;
import com.leicx.weixin.enums.OperateTypeEnum;
import com.leicx.weixin.mapper.FriendsRequestMapper;
import com.leicx.weixin.netty.UserChannelRel;
import com.leicx.weixin.netty.pojo.DataContent;
import com.leicx.weixin.pojo.vo.UsersVO;
import com.leicx.weixin.service.FriendsRequestService;
import com.leicx.weixin.service.MyFriendsService;
import com.leicx.weixin.util.JsonUtils;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class FriendsRequestServiceImpl implements FriendsRequestService {

    @Autowired
    private FriendsRequestMapper friendsRequestMapper;
    @Autowired
    private Sid sid;
    @Autowired
    private MyFriendsService myFriendsService;

    @Override
    public void insert(FriendsRequest friendsRequest) {
        friendsRequestMapper.insert(friendsRequest);
    }

    @Override
    public Integer getFriendRequestCountBySendUserId(String myUserId) {
        return friendsRequestMapper.getFriendRequestCountBySendUser(myUserId);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    @Override
    public void handleFriendRequest(String myUserId, String friendUserId, Integer operateType) {
        // 判断操作类型
        if (Objects.equals(operateType, OperateTypeEnum.ACCEPT.getCode())) {
            // 同意
            // 互相添加好友
            myFriendsService.insert(myUserId, friendUserId);
            myFriendsService.insert(friendUserId, myUserId);

            // 推送好友消息到接收方
            Channel channel = UserChannelRel.get(friendUserId);
            if (channel != null) {
                // 构造返回DataContent
                DataContent dataContent = new DataContent();
                dataContent.setAction(MsgTypeEnum.PULL_FRIENDS.getMsgCode());
                channel.writeAndFlush(new TextWebSocketFrame(JsonUtils.objectToJson(dataContent)));
            }
        }
        // 删除好友请求
        friendsRequestMapper.deleteFriendRequest(friendUserId, myUserId);
    }

    @Override
    public List<UsersVO> getListBySendUserIdJoinUsers(String acceptUserId) {
        return friendsRequestMapper.getListBySendUserIdJoinUsers(acceptUserId);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public FriendsRequest sendFriendsRequest(String sendUserId, String acceptUserId) {
        FriendsRequest friendsRequest = friendsRequestMapper.getFriendsRequestByIds(sendUserId, acceptUserId);
        if (null == friendsRequest) {
            // 新建记录
            friendsRequest = new FriendsRequest();
            String id = sid.nextShort();
            friendsRequest.setId(id);
            friendsRequest.setSendUserId(sendUserId);
            friendsRequest.setAcceptUserId(acceptUserId);
            friendsRequest.setRequestDatetime(new Date());
            insert(friendsRequest);
        }
        return friendsRequest;
    }
}
