package com.leicx.weixin.pojo.bo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 返回给前端的用户VO
 * @author daxiong
 * @date 2019-10-12 15:14
 * @return
 **/
@Getter
@Setter
@ToString
public class UsersBO {
    /**
     * 用户id
     */
    private String id;

    /**
     * 头像base64数据
     */
    private String faceData;

    /**
     * 用户昵称
     */
    private String nickname;
}
