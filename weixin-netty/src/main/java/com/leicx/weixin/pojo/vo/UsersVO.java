package com.leicx.weixin.pojo.vo;

import lombok.Getter;
import lombok.Setter;

/**
 * 返回给前端的用户VO
 * @author daxiong
 * @date 2019-10-12 15:14
 * @return
 **/
@Getter
@Setter
public class UsersVO {
    private String id;

    private String username;

    private String faceImg;

    private String faceImgBig;

    private String nickname;

    private String qrcode;

    private String cid;

}
