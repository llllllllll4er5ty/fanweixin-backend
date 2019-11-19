package com.leicx.weixin.service.impl;

import com.leicx.weixin.entity.MyFriends;
import com.leicx.weixin.entity.Users;
import com.leicx.weixin.enums.ErrorCodeEnum;
import com.leicx.weixin.mapper.UsersMapper;
import com.leicx.weixin.pojo.bo.UsersBO;
import com.leicx.weixin.service.MyFriendsService;
import com.leicx.weixin.service.UserService;
import com.leicx.weixin.util.FastDFSClient;
import com.leicx.weixin.util.FileUtils;
import com.leicx.weixin.util.LcxJSONResult;
import com.leicx.weixin.util.MD5Util;
import com.leicx.weixin.util.QRCodeUtils;
import com.leicx.weixin.util.StringUtils;
import org.n3r.idworker.Sid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UsersMapper usersMapper;
    @Autowired
    private Sid sid;
    @Autowired
    private FastDFSClient fastDFSClient;
    @Value("${fdfs.thumb-image.width}")
    private Integer width;
    @Value("${fdfs.thumb-image.height}")
    private Integer height;
    @Autowired
    private QRCodeUtils qrCodeUtils;
    @Autowired
    private MyFriendsService myFriendsService;

    @Override
    public LcxJSONResult checkLength(Users user) {
        String username = user.getUsername();
        String pwd = user.getPassword();
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(pwd)) {
            return LcxJSONResult.errorException(ErrorCodeEnum.ERROR_CODE_200002);
        }

        if (username.length() > 12 || pwd.length() > 12) {
            return LcxJSONResult.errorException(ErrorCodeEnum.ERROR_CODE_200005);

        }
        return LcxJSONResult.ok();
    }

    @Override
    public Users getUserByNameAndPwd(String name, String pwd) {
        return usersMapper.getUserByNameAndPwd(name, pwd);
    }

    @Override
    public Users getUserByKey(String key) {
        return usersMapper.getUserByKey(key);
    }

    @Override
    public Users getUserByName(String name) {
        Users userByName = usersMapper.getUserByName(name);
        return userByName;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public Users registerUser(Users user) {
        usersMapper.insertUser(user);
        return user;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public void updateUser(Users user) {
        usersMapper.updateUser(user);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS)
    public LcxJSONResult verifyUserToAdd(Users me, Users user) {
        if (null == user) {
            return LcxJSONResult.errorException(ErrorCodeEnum.ERROR_CODE_200007);
        }
        String meUsername = me.getUsername();
        String searchUsername = user.getUsername();
        if (Objects.equals(meUsername, searchUsername)) {
            return LcxJSONResult.errorException(ErrorCodeEnum.ERROR_CODE_200008);
        }

        // 查找是否已经是自己的好友
        MyFriends myFriendsByIds = myFriendsService.getMyFriendsByIds(me.getId(), user.getId());
        if (null != myFriendsByIds) {
            return LcxJSONResult.errorException(ErrorCodeEnum.ERROR_CODE_200009);
        }

        return LcxJSONResult.ok();
    }

    @Override
    public Users setUserFaceImg(UsersBO usersBO) throws Exception {
        String userId = usersBO.getId();
        String faceData = usersBO.getFaceData();
        // 根据用户id获取对应的用户
        Users userByKey = getUserByKey(userId);

        String filePath = "/Users/daxiong/lcx/fangweixin/tmp/" + userId + ".png";

        // base64转换成文件
        FileUtils.base64ToFile(filePath, faceData);
        // 文件路径转换成MultipartFile类型文件
        MultipartFile faceFile = FileUtils.fileToMultipart(filePath);

        // 上传文件到fastdfs
        String faceUrl = fastDFSClient.uploadBase64(faceFile);
        System.out.println(faceUrl);
//        "fadfadfaa.png"
//        "fadfadfaa_80x80.png" fastdfs自动生成的缩略图
        // 获取缩略图url
        String[] split = faceUrl.split("\\.");
        String thump = "_" + width + "x" + height + ".";
        String thumpImgUrl = split[0] + thump + split[1];

        userByKey.setFaceImg(thumpImgUrl);
        userByKey.setFaceImgBig(faceUrl);
        return userByKey;
    }

    @Override
    public void initUser(Users user) throws IOException {
        String userId = sid.nextShort();
        user.setId(userId);
        user.setNickname(user.getUsername());
        user.setPassword(MD5Util.EncoderByMd5(user.getPassword()));
        user.setFaceImg("");
        user.setFaceImgBig("");
        user.setQrcode(setQrCode(user.getUsername()));
    }

    private String setQrCode(String username) throws IOException {
        String filePath = "/Users/daxiong/lcx/fangweixin/tmp/qr/" + username + ".png";
        File file = new File(filePath);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            parentFile.mkdirs();
        }
        file.createNewFile();

        qrCodeUtils.createQRCode(filePath, "xiongxin_qrcode:" + username);
        MultipartFile qrFile = FileUtils.fileToMultipart(filePath);
        // 上传文件到fastdfs
        String qrUrl = fastDFSClient.uploadQRCode(qrFile);
        System.out.println(qrUrl);
        return qrUrl;
    }
}
