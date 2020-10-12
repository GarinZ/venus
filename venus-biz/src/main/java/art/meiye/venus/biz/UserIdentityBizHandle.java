package art.meiye.venus.biz;

import art.meiye.venus.biz.dto.UserIdentityCondition;
import art.meiye.venus.common.domain.UserIdentityTypeEnum;
import art.meiye.venus.dal.entity.UserIdentity;
import art.meiye.venus.dal.redis.verificationcode.VerificationEnum;

import java.util.List;

/**
 * 业务逻辑操作 - 用户认证
 * @author Garin
 * @date 2020-08-14
 */
public interface UserIdentityBizHandle {
    /** 加密盐长度 */
    int SALT_LENGTH = 8;
    /** 登录失败阈值 */
    long LOGIN_FAIL_THRESHOLD = 10L;
    /** 登录失败锁定时间（毫秒） - 15分钟 */
    long LOGIN_FORBIDDEN_MS = 15 * 60 * 1000;
    /** 验证码校验失败阈值 */
    long VERIFICATION_FAIL_THRESHOLD = 10L;

    /**
     * Email是否已注册
     * @param email 邮箱
     * @return 是否已经注册
     */
    Boolean isEmailExist(String email);

    /**
     * UserIdentity记录通用查询方法
     * @param condition 查询条件DTO
     * @return 查询记录列表
     */
    List<UserIdentity> queryUserIdentity(UserIdentityCondition condition);

    /**
     * 校验邮箱和验证码是否匹配
     * @param email 邮箱
     * @param code 验证码
     * @return 是否匹配
     */
    Boolean isEmailAndVerificationCodeMatches(String email, String code);

    /**
     * 新增邮箱用户认证信息
     * @param userId 用户id
     * @param email 邮箱
     * @param password 密码
     * @return 新增认证记录
     */
    UserIdentity addEmailUser(Integer userId, String email, String password);

    /**
     * 新增微信用户认证记录
     * @param userId 用户Id
     * @param unionId 微信unionId
     * @return 新增认证记录
     */
    UserIdentity addWechatUser(Integer userId, String unionId);

    /**
     * 获取token
     * 存在则获取，不存在则设置
     * @param userId 用户id
     * @return token
     */
    String getToken(Integer userId);

    /**
     * 邮箱密码是否匹配
     * @param email 邮箱
     * @param password 密码
     * @return 是否匹配
     */
    Boolean isEmailAndPasswordMatches(String email, String password);

    /**
     * 是否禁用登录
     * @param email 邮箱
     * @return key存在：禁用登录时间（秒）；key不存在：返回{@link art.meiye.venus.dal.redis.RedisResponseEnum#EXPIRE_EMPTY}
     */
    Long isForbiddenLogin(String email);

    /**
     * 处理登录失败情况
     * 1. 增加登录失败的计数器
     * 2. 失败次数达到阈值{@link this#LOGIN_FAIL_THRESHOLD}
     * @param email 邮箱
     */
    void processLoginFail(String email);

    /**
     * 处理校验成功情况
     * 1. 重置登录失败计数器
     * @param email 邮箱
     */
    void processLoginSuccess(String email);

    /**
     * 是否禁止验证码校验
     * @param identity id
     * @param verificationEnum 验证码类型枚举
     * @return 是否禁止
     */
    Boolean isForbiddenVerificationValidate(String identity, VerificationEnum verificationEnum);

    /**
     * 处理验证码校验失败
     * @param identity id
     * @param verificationEnum 验证码类型枚举
     */
    void processVerificationFail(String identity, VerificationEnum verificationEnum);

    /**
     * 处理验证码校验成功
     * @param identity id
     * @param verificationEnum 验证码类型枚举
     */
    void processVerificationSuccess(String identity, VerificationEnum verificationEnum);

    /**
     * 重置密码
     * @param identity id
     * @param userIdentityTypeEnum 身份枚举
     * @param password 密码
     */
    void resetPassword(String identity, UserIdentityTypeEnum userIdentityTypeEnum, String password);

    /**
     * token是否可用
     * @param userId 用户id
     * @param token token
     * @return 是否可用
     */
    Boolean isTokenValid(Integer userId, String token);

    /**
     * 解绑用户认证方式
     * @param condition 查询条件
     */
    void deleteUserIdentityByCondition(UserIdentityCondition condition);

}
