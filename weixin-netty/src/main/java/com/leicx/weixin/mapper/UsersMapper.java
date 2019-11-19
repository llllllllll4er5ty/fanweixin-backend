package com.leicx.weixin.mapper;

import com.leicx.weixin.entity.Users;
import com.leicx.weixin.util.IBaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersMapper extends IBaseMapper<Users> {
    /**
     * 根据用户名获取用户
     * @param name
     * @return
     */
    Users getUserByName(@Param("name")String name);
    /**
     * 根据用户id获取用户
     * @param key
     * @return
     */
    Users getUserByKey(@Param("key")String key);
    /**
     * 根据用户名和密码获取用户
     * @param name
     * @return
     */
    Users getUserByNameAndPwd(@Param("name")String name, @Param("pwd")String pwd);
    /**
     * 插入用户
     * @param user
     * @return
     */
    Integer insertUser(@Param("user")Users user);

    /**
     * 更新用户
     * @param user
     */
    void updateUser(Users user);
}