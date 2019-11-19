package com.leicx.weixin.controller;

import com.leicx.weixin.entity.Users;
import com.leicx.weixin.enums.ErrorCodeEnum;
import com.leicx.weixin.pojo.bo.UsersBO;
import com.leicx.weixin.pojo.vo.UsersVO;
import com.leicx.weixin.service.FriendsRequestService;
import com.leicx.weixin.service.MyFriendsService;
import com.leicx.weixin.service.UserService;
import com.leicx.weixin.util.LcxJSONResult;
import com.leicx.weixin.util.MD5Util;
import com.leicx.weixin.util.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;

import javax.annotation.Resource;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * 用户Controller
 * @author daxiong
 * @date 2019-10-14 10:55
 **/
@RestController
@RequestMapping("/u")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private FriendsRequestService friendsRequestService;
    @Resource
    private MyFriendsService myFriendsService;

    @PostMapping("/loginOrRegister")
    public LcxJSONResult loginOrRegister(@RequestBody Users user) {
        if (user == null) {
            return LcxJSONResult.errorException(ErrorCodeEnum.ERROR_CODE_200001);
        }
        // 用户名密码校验
        LcxJSONResult lcxJSONResult = userService.checkLength(user);
        if (!lcxJSONResult.getCode().equals(LcxJSONResult.SUCCESS_CODE)) {
            return lcxJSONResult;
        }

        String username = user.getUsername();
        String password = user.getPassword();
        // 判断是注册还是登陆
        Users userByName = userService.getUserByName(username);
        // 返回VO对象
        UsersVO usersVO = new UsersVO();
        if (userByName == null) {
            // 注册
            try {
                userService.initUser(user);
            } catch (IOException e) {
                e.printStackTrace();
                return LcxJSONResult.errorException(ErrorCodeEnum.ERROR_CODE_210002);
            }
            userService.registerUser(user);
        } else {
            // 登录
            // 判断用户名和密码是否正确
            user = userService.getUserByNameAndPwd(username, MD5Util.EncoderByMd5(password));
            if (user == null) {
                return LcxJSONResult.errorException(ErrorCodeEnum.ERROR_CODE_200004);
            }
        }
        BeanUtils.copyProperties(user, usersVO);

        return LcxJSONResult.ok(usersVO);
    }

    @PostMapping("/uploadFaceImg")
    public LcxJSONResult uploadFaceImg(@RequestBody UsersBO usersBO) {
        if (usersBO == null) {
            return LcxJSONResult.errorException(ErrorCodeEnum.ERROR_CODE_200001);
        }

        try {
            // 设置头像
            Users user = userService.setUserFaceImg(usersBO);
            // 更新用户
            userService.updateUser(user);
            UsersVO usersVO = new UsersVO();

            // 返回给前端
            BeanUtils.copyProperties(user, usersVO);
            return LcxJSONResult.ok(usersVO);
        } catch (Exception e) {
            e.printStackTrace();
            return LcxJSONResult.errorException(ErrorCodeEnum.ERROR_CODE_210001);
        }
    }

    @PostMapping("/updateNickname")
    public LcxJSONResult updateNickname(@RequestBody UsersBO usersBO) {
        if (usersBO == null) {
            return LcxJSONResult.errorException(ErrorCodeEnum.ERROR_CODE_200001);
        }

        try {
            // 设置昵称
            String id = usersBO.getId();
            String nickname = usersBO.getNickname();
            if (StringUtils.isEmpty(id) || StringUtils.isEmpty(nickname)) {
                return LcxJSONResult.errorException(ErrorCodeEnum.ERROR_CODE_100002);
            }
            if (nickname.length() > 12) {
                return LcxJSONResult.errorException(ErrorCodeEnum.ERROR_CODE_200006);
            }
            Users userByKey = userService.getUserByKey(id);
            userByKey.setNickname(nickname);
            // 更新用户
            userService.updateUser(userByKey);
            UsersVO usersVO = new UsersVO();

            // 返回给前端
            BeanUtils.copyProperties(userByKey, usersVO);
            return LcxJSONResult.ok(usersVO);
        } catch (Exception e) {
            e.printStackTrace();
            return LcxJSONResult.errorException(ErrorCodeEnum.ERROR_CODE_210001);
        }
    }

    @PostMapping("/searchUser")
    public LcxJSONResult searchUser(@RequestParam String myUserId,
                                        @RequestParam String searchUsername) {
        if (StringUtils.isEmpty(myUserId) || StringUtils.isEmpty(searchUsername)) {
            return LcxJSONResult.errorException(ErrorCodeEnum.ERROR_CODE_100002);
        }

        Users me = userService.getUserByKey(myUserId);
        // 根据账号做匹配查询，而不是模糊查询
        Users searchUser = userService.getUserByName(searchUsername);
        // 判断搜索的用户是否符合添加好友的标准
        LcxJSONResult lcxJSONResult = userService.verifyUserToAdd(me, searchUser);
        if (lcxJSONResult.getCode().equals(LcxJSONResult.SUCCESS_CODE)) {
            // 符合标准，返回被搜索用户的信息
            UsersVO usersVO = new UsersVO();
            // 返回给前端
            BeanUtils.copyProperties(searchUser, usersVO);
            return LcxJSONResult.ok(usersVO);
        }
        // 不符合标准，返回错误信息
        return lcxJSONResult;
    }

    @PostMapping("/addFriendRequest")
    public LcxJSONResult addFriendRequest(@RequestParam String myUserId,
                                        @RequestParam String searchUsername) {
        if (StringUtils.isEmpty(myUserId) || StringUtils.isEmpty(searchUsername)) {
            return LcxJSONResult.errorException(ErrorCodeEnum.ERROR_CODE_100002);
        }

        Users me = userService.getUserByKey(myUserId);
        // 根据账号做匹配查询，而不是模糊查询
        Users searchUser = userService.getUserByName(searchUsername);
        // 判断搜索的用户是否符合添加好友的标准
        LcxJSONResult lcxJSONResult = userService.verifyUserToAdd(me, searchUser);
        if (lcxJSONResult.getCode().equals(LcxJSONResult.SUCCESS_CODE)) {
            // 符合标准，发送friendsRequest请求
            friendsRequestService.sendFriendsRequest(myUserId, searchUser.getId());
        }
        // 不符合标准，返回错误信息
        return lcxJSONResult;
    }

    @PostMapping("/getFriendRequestList")
    public LcxJSONResult getFriendRequestList(@RequestParam String myUserId) {
        if (StringUtils.isEmpty(myUserId)) {
            return LcxJSONResult.errorException(ErrorCodeEnum.ERROR_CODE_100002);
        }

        List<UsersVO> lists = friendsRequestService.getListBySendUserIdJoinUsers(myUserId);
        if (null == lists) {
            lists = new ArrayList<>();
        }
        return LcxJSONResult.ok(lists);
    }
    @GetMapping("/getFriendRequestCount")
    public LcxJSONResult getFriendRequestCount(String myUserId) {
        if (StringUtils.isEmpty(myUserId)) {
            return LcxJSONResult.errorException(ErrorCodeEnum.ERROR_CODE_100002);
        }

        Integer count = friendsRequestService.getFriendRequestCountBySendUserId(myUserId);
        if (null == count) {
            count = 0;
        }
        return LcxJSONResult.ok(count);
    }

    /**
     * 处理好友请求
     * @author daxiong
     * @date 2019-10-30 11:06
     * @param myUserId
     * @param friendUserId
     * @param operateType   操作类型，0：拒绝；1：接收
     * @return com.leicx.weixin.util.LcxJSONResult
     **/
    @PostMapping("/handleFriendRequest")
    public LcxJSONResult handleFriendRequest(@RequestParam String myUserId,
                                             @RequestParam String friendUserId,
                                             @RequestParam Integer operateType) {
        if (StringUtils.isEmpty(myUserId) || StringUtils.isEmpty(friendUserId)
                || null == operateType) {
            return LcxJSONResult.errorException(ErrorCodeEnum.ERROR_CODE_100002);
        }

        friendsRequestService.handleFriendRequest(myUserId, friendUserId, operateType);

        return LcxJSONResult.ok();
    }

    /**
     * 获取我的好友列表
     * @author daxiong
     * @date 2019-10-31 16:14
     * @param userId
     * @return com.leicx.weixin.util.LcxJSONResult
     **/
    @PostMapping("/getMyFriendsList")
    public LcxJSONResult getMyFriendsList(@RequestParam String userId) {
        if (StringUtils.isEmpty(userId)) {
            return LcxJSONResult.errorException(ErrorCodeEnum.ERROR_CODE_100002);
        }

        List<UsersVO> friendsList = myFriendsService.getMyFriendsList(userId);

        return LcxJSONResult.ok(friendsList);
    }

    @GetMapping("/test")
    public String test() {

        ArrayBlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(20);
        ExecutorService executorService = new ThreadPoolExecutor(3, 5, 50, TimeUnit.MILLISECONDS, queue);

//        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2);
        Future<String> future = executorService.submit(() -> {
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    System.out.println("任务被中断");
                }
                return "OK";
        });
        try {
            future.get(5, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            // 参数为true则会强制interrupt中断
            future.cancel(true);
            e.printStackTrace();
            System.out.println("任务超时");
            return "任务超时";
        } finally {
            System.out.println("清理资源");
        }
        return "SUCCESS";
    }

    @GetMapping("/test1")
    public Callable<LcxJSONResult> test1() {
        return () -> {
            try {
               timeOut();
            } catch (SocketTimeoutException | AsyncRequestTimeoutException e) {
                e.printStackTrace();
                System.out.println("超时");
                return LcxJSONResult.errorException("超时");
            }
            System.out.println("SUCCESS");
            return LcxJSONResult.ok("SUCCESS");
        };
    }
    public void timeOut() throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        // 请求第三方接口
        HttpGet httpGet = new HttpGet("http://localhost:8082/springboot/hello");
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(5000)
                .setConnectTimeout(5000)
                .setSocketTimeout(5000).build();
        httpGet.setConfig(requestConfig);
        CloseableHttpResponse response = httpClient.execute(httpGet);
        if (response.getStatusLine().getStatusCode() == 200) {
            HttpEntity entity = response.getEntity();
            // 使用工具类EntityUtils，从响应中取出实体表示的内容并转换成字符串
            String string = EntityUtils.toString(entity, "utf-8");
            System.out.println(string);
        }
        response.close();
        httpClient.close();
    }

}
