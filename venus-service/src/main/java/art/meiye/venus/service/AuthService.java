package art.meiye.venus.service;

import art.meiye.venus.biz.UserBizHandle;
import art.meiye.venus.biz.UserIdentityBizHandle;
import art.meiye.venus.biz.dto.UserIdentityCondition;
import art.meiye.venus.common.api.ErrorMapping;
import art.meiye.venus.common.constants.Constants;
import art.meiye.venus.common.domain.UserIdentityTypeEnum;
import art.meiye.venus.common.exceptions.BizException;
import art.meiye.venus.dal.entity.User;
import art.meiye.venus.dal.entity.UserIdentity;
import art.meiye.venus.dal.redis.verificationcode.VerificationCodeStorage;
import art.meiye.venus.dal.redis.verificationcode.VerificationEnum;
import art.meiye.venus.dal.utils.UserUtils;
import art.meiye.venus.sal.email.EmailSenderService;
import art.meiye.venus.sal.wechat.WechatService;
import art.meiye.venus.sal.wechat.dto.AccessTokenResponse;
import art.meiye.venus.sal.wechat.dto.UserProfileQueryResponse;
import art.meiye.venus.service.dto.UserIdentityDTO;
import art.meiye.venus.service.vo.auth.*;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.RandomUtil;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

/**
 * @author Garin
 * @date 2020-08-09
 */

@Service
public class AuthService {
    /** 邮箱验证码长度 */
    private static final int EMAIL_VERIFICATION_SIZE = 9;

    @Autowired
    EmailSenderService emailSenderService;

    @Autowired
    VerificationCodeStorage verificationCodeStorage;

    @Autowired
    UserIdentityBizHandle userIdentityBizHandle;

    @Autowired
    UserBizHandle userBizHandle;

    @Autowired
    WechatService wechatService;

    @Value("${wechat.appId}")
    private String appId;

    @Value("${wechat.secretId}")
    private String secretId;

    @Value("${wechat.stateKey}")
    private String stateKey;

    @Value("${wechat.bindStateToken}")
    private String bindStateToken;

    /**
     * 发送邮箱验证码
     * @param request
     */
    public void sendVerificationEmail(VerificationCodeSendRequest request) {
        // 1. 生成9位随机数
        int[] ints = NumberUtil.generateRandomNumber(0, 9, EMAIL_VERIFICATION_SIZE);
        String verificationCode = StringUtils.join(ArrayUtils.toObject(ints), Constants.EMPTY_STRING);
        String toAddress = request.getEmail();
        String subject = "美叶账号注册验证";
        String text = "亲爱的美叶用户您好！\n" +
                "您本次邮箱注册的验证码是："+
                 verificationCode +
                "\n" +
                "请勿将验证码透露给其他人。\n" +
                "\n" +
                "本邮件由系统自动发送，请勿直接回复！\n" +
                "感谢您的访问，祝您使用愉快！\n";
        // 2. 发送邮件
        emailSenderService.sendMail(toAddress, subject, text);
        // 3. 验证码存入Redis并设置重发锁
        verificationCodeStorage.setVerificationCode(toAddress, verificationCode, VerificationEnum.EMAIL);
    }

    /**
     * 邮箱注册
     * @param request 请求参数
     * @return token & userid
     */
    @Transactional(rollbackFor = Exception.class)
    public EmailRegisterResponse emailRegister(EmailRegisterRequest request) {
        // 1. 写入user表
        User user = new User();
        user.setNickname(UserBizHandle.NICK_NAME_DEFAULT_PREFIX
                + RandomUtil.randomNumbers(UserBizHandle.NICK_NAME_DEFAULT_SUFFIX_LENGTH));
        user.setAvatarurl(UserBizHandle.DEFAULT_AVATAR);
        user = userBizHandle.addUser(user);
        Integer userId = user.getId();
        // 2. 写入userIdentity表
        userIdentityBizHandle.addEmailUser(userId, request.getEmail(), request.getPassword());
        // 3. 获取身份token
        String token = userIdentityBizHandle.getToken(userId);
        EmailRegisterResponse emailRegisterResponse = new EmailRegisterResponse();
        emailRegisterResponse.setToken(token);
        emailRegisterResponse.setUserId(userId);
        return emailRegisterResponse;
    }

    /**
     * 邮箱登录
     * @param request 请求参数
     * @return token & userid
     */
    public EmailLoginResponse emailLogin(EmailLoginRequest request) {
        UserIdentityCondition condition = new UserIdentityCondition();
        condition.setUserIdentityType(UserIdentityTypeEnum.EMAIL);
        condition.setIdentifier(request.getEmail());
        List<UserIdentity> userIdentities = userIdentityBizHandle.queryUserIdentity(condition);
        Integer userId = userIdentities.get(0).getUserId();
        String token = userIdentityBizHandle.getToken(userId);
        EmailLoginResponse emailLoginResponse = new EmailLoginResponse();
        emailLoginResponse.setToken(token);
        emailLoginResponse.setUserId(userId);
        return emailLoginResponse;
    }

    /**
     * 邮箱密码重置
     * @param request 请求参数
     */
    @Transactional(rollbackFor = Exception.class)
    public void emailPasswordReset(PasswordResetRequest request) {
        userIdentityBizHandle.resetPassword(request.getEmail(), UserIdentityTypeEnum.EMAIL, request.getPassword());
    }

    @Transactional(rollbackFor = Exception.class)
    public UserIdentityDTO wechatLogin(String code) {
        // 1. 请求AccessToken
        Optional<AccessTokenResponse> accessTokenResOpt = wechatService.getAccessToken(appId, secretId, code);
        if (!accessTokenResOpt.isPresent()) {
            // error: accessToken请求失败
            throw new BizException(ErrorMapping.ACCOUNT_WECHAT_LOGIN_ERROR);
        }
        AccessTokenResponse accessTokenResponse = accessTokenResOpt.get();
        // 2. 判断是否为新用户
        String unionId = accessTokenResponse.getUnionId();
        UserIdentityCondition condition = new UserIdentityCondition();
        condition.setUserIdentityType(UserIdentityTypeEnum.WECHAT);
        condition.setIdentifier(unionId);
        List<UserIdentity> userIdentities = userIdentityBizHandle.queryUserIdentity(condition);
        // 3. 获取用户信息
        Optional<UserProfileQueryResponse> userProfileOpt = wechatService.getUserProfile(
                accessTokenResponse.getAccessToken(), accessTokenResponse.getOpenId());
        if (!userProfileOpt.isPresent()) {
            // error: userProfile请求失败
            throw new BizException(ErrorMapping.ACCOUNT_WECHAT_LOGIN_ERROR);
        }
        boolean isNewUser = false;
        if (CollectionUtils.isEmpty(userIdentities)) {
            isNewUser = true;
        }
        // 4. 用户信息处理
        Integer userId = null;
        if (isNewUser) {
            // 4.1 新用户 - insert user, insert user identity
            User newUser = UserUtils.buildUser(userProfileOpt.get());
            userBizHandle.addUser(newUser);
            userId = newUser.getId();
            userIdentityBizHandle.addWechatUser(newUser.getId(), unionId);
        } else {
            // 4.2 老用户 - update user
            userId = userIdentities.get(0).getUserId();
            User updatedUser = UserUtils.buildUser(userId, userProfileOpt.get());
            userBizHandle.updateDefaultUserInfo(userId, updatedUser);
        }
        UserIdentityDTO userIdentityDTO = new UserIdentityDTO();
        String token = userIdentityBizHandle.getToken(userId);
        userIdentityDTO.setToken(token);
        userIdentityDTO.setUserId(userId);
        return userIdentityDTO;
    }

}
