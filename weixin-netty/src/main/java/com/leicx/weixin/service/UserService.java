package com.leicx.weixin.service;

import com.leicx.weixin.entity.Users;
import com.leicx.weixin.pojo.bo.UsersBO;
import com.leicx.weixin.util.LcxJSONResult;

import java.io.IOException;

/**
 * 用户service接口
 */
public interface UserService {
    /**
     * 校验用户名/密码的长度
     * @param user
     * @return
     */
    LcxJSONResult checkLength(Users user);

    /**
     * 根据用户名获取用户
     * @param name
     * @return
     */
    Users getUserByName(String name);

    /**
     * 根据用户id获取用户
     * @param key
     * @return
     */
    Users getUserByKey(String key);

    /**
     * 根据用户名和密码获取用户
     * @param name
     * @return
     */
    Users getUserByNameAndPwd(String name, String pwd);

    /**
     * 注册用户
     * @param user
     * @return
     */
    Users registerUser(Users user);

    /**
     * 注册用户初始化
     * @param user
     * @return
     */
    void initUser(Users user) throws IOException;

    /**
     * 设置用户头像
     * @param usersBO
     * @return
     */
    Users setUserFaceImg(UsersBO usersBO) throws Exception;

    /**
     * 更新用户
     * @param user
     */
    void updateUser(Users user);

    /**
     * 判断被搜索的用户是否符合添加好友的标准
     * 1、被添加人存在；
     * 2、被添加人不能是自己；
     * 2、被添加人与自己不能已经是好友关系；
     * @param me    自己
     * @param user  被搜索的用户
     * @return
     */
    LcxJSONResult verifyUserToAdd(Users me, Users user);
}
