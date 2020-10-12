package art.meiye.venus.service;

import art.meiye.venus.biz.UserBizHandle;
import art.meiye.venus.biz.UserIdentityBizHandle;
import art.meiye.venus.biz.dto.UserCondition;
import art.meiye.venus.biz.dto.UserIdentityCondition;
import art.meiye.venus.common.api.ErrorMapping;
import art.meiye.venus.common.domain.UserIdentityTypeEnum;
import art.meiye.venus.common.exceptions.BizException;
import art.meiye.venus.common.utils.ThreadContextHolder;
import art.meiye.venus.dal.entity.User;
import art.meiye.venus.dal.entity.UserIdentity;
import art.meiye.venus.dal.utils.UserUtils;
import art.meiye.venus.sal.upload.UploadService;
import art.meiye.venus.sal.wechat.WechatService;
import art.meiye.venus.sal.wechat.dto.AccessTokenResponse;
import art.meiye.venus.sal.wechat.dto.UserProfileQueryResponse;
import art.meiye.venus.service.vo.account.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 账户服务
 * @author Garin
 * @date 2020-08-26
 */
@Slf4j
@Service
public class AccountService {
    @Value("${wechat.appId}")
    private String appId;

    @Value("${wechat.secretId}")
    private String secretId;

    @Value("${wechat.stateKey}")
    private String stateKey;

    @Value("${wechat.bindStateToken}")
    private String bindStateToken;

    @Autowired
    WechatService wechatService;

    @Autowired
    UserBizHandle userBizHandle;

    @Autowired
    UserIdentityBizHandle userIdentityBizHandle;

    @Autowired
    UploadService uploadService;

    /**
     * 获取用户信息
     * @param userId 用户id
     * @return 用户信息
     */
    public UserProfileResponse getUserProfile(Integer userId) {
        UserCondition userCondition = new UserCondition();
        userCondition.setId(userId);
        User user = userBizHandle.getSingleUserInfoByCondition(userCondition);
        UserIdentityCondition identityCondition = new UserIdentityCondition();
        identityCondition.setUserId(Collections.singletonList(userId));
        List<UserIdentity> userIdentities = userIdentityBizHandle.queryUserIdentity(identityCondition);
        UserProfileResponse response = new UserProfileResponse();
        response.setAvatarUrl(user.getAvatarurl());
        response.setUserId(userId);
        response.setUserName(user.getNickname());
        response.setIsWechatBind(false);
        response.setIsEmailBind(false);
        userIdentities.forEach(item -> {
            if (UserIdentityTypeEnum.WECHAT.getValue().equals(item.getUserIdentityType())) {
                response.setIsWechatBind(true);
            } else if (UserIdentityTypeEnum.EMAIL.getValue().equals(item.getUserIdentityType())) {
                response.setIsEmailBind(true);
            }
        });

        return response;
    }

    /**
     * 更新用户信息
     * 按需更新，字段为null则不更新
     * @param userId 用户id
     * @param request 请求
     * @return 返回
     */
    public UserProfileResponse updateUserProfile(Integer userId, UserProfileUpdateRequest request) {
        User user = new User();
        user.setId(userId);
        user.setNickname(request.getUserName());
        user.setAvatarurl(request.getAvatarUrl());
        userBizHandle.updateUserSelective(user);
        UserProfileResponse response = new UserProfileResponse();
        response.setUserName(request.getUserName());
        response.setAvatarUrl(request.getAvatarUrl());
        response.setUserId(userId);
        return response;
    }

    /**
     * base64字符串上传图片
     * @param userId userId
     * @param request 请求
     * @throws BizException 图片上传异常
     * @return 返回
     */
    public ImageUploadResponse uploadImageWithBase64(Integer userId, ImageUploadRequest request) throws BizException {
        byte[] decodedBase64 = null;
        try {
            decodedBase64 = Base64.getDecoder().decode(request.getBase64());
        } catch (Exception e) {
            log.error("字符串转base64解析错误", e);
            throw new BizException(ErrorMapping.IMAGE_UPLOAD_PARSE_ERROR_FAIL);
        }
        Optional<String> urlOpt = uploadService.uploadFile(decodedBase64);
        if (!urlOpt.isPresent()) {
            throw new BizException(ErrorMapping.IMAGE_UPLOAD_FAIL);
        }
        ImageUploadResponse response = new ImageUploadResponse();
        response.setUrl(urlOpt.get());
        return response;
    }

    /**
     * 将微信绑定到当前登录账号
     * @param code 微信登录成功回调的code
     * @throws BizException 当{accessToken请求失败 | 微信已称为独立账户或绑定在其他账号下 | userProfile请求失败 } 会抛出异常
     */
    @Transactional(rollbackFor = Exception.class)
    public void bindWechat(String code) throws BizException {
        // 1. 请求AccessToken
        Optional<AccessTokenResponse> accessTokenResOpt = wechatService.getAccessToken(appId, secretId, code);
        if (!accessTokenResOpt.isPresent()) {
            // error: accessToken请求失败
            throw new BizException(ErrorMapping.ACCOUNT_WECHAT_BIND_UNEXPECT_ERROR);
        }
        AccessTokenResponse accessTokenResponse = accessTokenResOpt.get();
        // 2. 校验unionId
        String unionId = accessTokenResponse.getUnionId();
        UserIdentityCondition userIdentityCondition = new UserIdentityCondition();
        userIdentityCondition.setIdentifier(unionId);
        userIdentityCondition.setUserIdentityType(UserIdentityTypeEnum.WECHAT);
        List<UserIdentity> userIdentities = userIdentityBizHandle.queryUserIdentity(userIdentityCondition);
        if (!CollectionUtils.isEmpty(userIdentities)) {
            // error: 微信已称为独立账户或绑定在其他账号下
            throw new BizException(ErrorMapping.ACCOUNT_WECHAT_BIND_EXIST);
        }
        // 3. 从微信接口获取用户信息
        Optional<UserProfileQueryResponse> userProfileOpt = wechatService.getUserProfile(
                accessTokenResponse.getAccessToken(), accessTokenResponse.getOpenId());
        if (!userProfileOpt.isPresent()) {
            // error: userProfile请求失败
            throw new BizException(ErrorMapping.ACCOUNT_WECHAT_BIND_UNEXPECT_ERROR);
        }
        UserProfileQueryResponse userProfile = userProfileOpt.get();
        // 5. 更新微信相关信息（包括上传用户头像）
        Integer userId = ThreadContextHolder.getUserId();
        User userUpdateDTO = UserUtils.buildUser(userId, userProfile);
        userBizHandle.updateDefaultUserInfo(userId, userUpdateDTO);
        // 6. 新增账户记录
        userIdentityBizHandle.addWechatUser(userId, unionId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void unbindWechat() throws BizException {
        Integer userId = ThreadContextHolder.getUserId();
        UserIdentityCondition condition = new UserIdentityCondition();
        condition.setUserId(Collections.singletonList(userId));
        List<UserIdentity> userIdentities = userIdentityBizHandle.queryUserIdentity(condition);
        if (userIdentities.size() == 1 && UserIdentityTypeEnum.WECHAT.getValue()
                .equals(userIdentities.get(0).getUserIdentityType())) {
            throw new BizException(ErrorMapping.ACCOUNT_WECHAT_UNBIND_INVALID);
        } else if (!userIdentities.stream().anyMatch(item -> UserIdentityTypeEnum.WECHAT.
                getValue().equals(item.getUserIdentityType()))) {
            throw new BizException(ErrorMapping.ACCOUNT_WECHAT_UNBIND_NOT_EXIST);
        }
        condition.setUserIdentityType(UserIdentityTypeEnum.WECHAT);
        userIdentityBizHandle.deleteUserIdentityByCondition(condition);
    }

    @Transactional(rollbackFor = Exception.class)
    public void bindEmail(Integer userId, EmailBindRequest request) throws BizException {
        userIdentityBizHandle.addEmailUser(userId, request.getEmail(), request.getPassword());
    }

    @Transactional(rollbackFor = Exception.class)
    public void unbindEmail() throws BizException {
        Integer userId = ThreadContextHolder.getUserId();
        UserIdentityCondition condition = new UserIdentityCondition();
        condition.setUserId(Collections.singletonList(userId));
        List<UserIdentity> userIdentities = userIdentityBizHandle.queryUserIdentity(condition);
        if (userIdentities.size() == 1 && UserIdentityTypeEnum.EMAIL.getValue()
                .equals(userIdentities.get(0).getUserIdentityType())) {
            throw new BizException(ErrorMapping.ACCOUNT_EMAIL_UNBIND_INVALID);
        } else if (!userIdentities.stream().anyMatch(item -> UserIdentityTypeEnum.WECHAT.
                getValue().equals(item.getUserIdentityType()))) {
            throw new BizException(ErrorMapping.ACCOUNT_EMAIL_UNBIND_NOT_EXIST);
        }
        condition.setUserIdentityType(UserIdentityTypeEnum.EMAIL);
        userIdentityBizHandle.deleteUserIdentityByCondition(condition);
    }
}
