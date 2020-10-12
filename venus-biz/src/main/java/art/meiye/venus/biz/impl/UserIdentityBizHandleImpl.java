package art.meiye.venus.biz.impl;

import art.meiye.venus.biz.UserIdentityBizHandle;
import art.meiye.venus.biz.dto.UserIdentityCondition;
import art.meiye.venus.common.constants.DeleteEnum;
import art.meiye.venus.common.domain.UserIdentityTypeEnum;
import art.meiye.venus.common.utils.CryptoUtils;
import art.meiye.venus.dal.entity.UserIdentity;
import art.meiye.venus.dal.entity.UserIdentityExample;
import art.meiye.venus.dal.mapper.UserIdentityMapper;
import art.meiye.venus.dal.mapper.UserMapper;
import art.meiye.venus.dal.redis.auth.AuthStorage;
import art.meiye.venus.dal.redis.verificationcode.VerificationCodeStorage;
import art.meiye.venus.dal.redis.verificationcode.VerificationEnum;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * UserIdentityBizHandle
 * @author Garin
 * @date 2020-08-11
 */

@Service
public class UserIdentityBizHandleImpl implements UserIdentityBizHandle {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserIdentityMapper userIdentityMapper;

    @Autowired
    VerificationCodeStorage verificationCodeStorage;

    @Autowired
    AuthStorage authStorage;

    @Override
    public Boolean isEmailExist(String email) {
        UserIdentityCondition condition = new UserIdentityCondition();
        condition.setUserIdentityType(UserIdentityTypeEnum.EMAIL);
        condition.setIdentifier(email);
        List<UserIdentity> userIdentities = queryUserIdentity(condition);
        return !CollectionUtils.isEmpty(userIdentities);
    }

    @Override
    public List<UserIdentity> queryUserIdentity(UserIdentityCondition condition) {
        UserIdentityExample example = buildExampleByCondition(condition);
        return userIdentityMapper.selectByExample(example);
    }

    @Override
    public Boolean isEmailAndVerificationCodeMatches(String email, String code) {
        return verificationCodeStorage.isVerificationCodeMatches(email, code, VerificationEnum.EMAIL);
    }

    @Override
    public UserIdentity addEmailUser(Integer userId, String email, String password) {
        UserIdentity userIdentity = new UserIdentity();
        userIdentity.setUserId(userId);
        userIdentity.setIdentifier(email);
        String salt = RandomUtil.randomString(SALT_LENGTH);
        userIdentity.setCredentialSalt(salt);
        String encodedPassword = CryptoUtils.md5Encrypt(salt, password);
        userIdentity.setCredential(encodedPassword);
        userIdentity.setUserIdentityType(UserIdentityTypeEnum.EMAIL.getValue());
        userIdentity.setIsDelete(DeleteEnum.FALSE.getValue());
        userIdentityMapper.insert(userIdentity);
        return userIdentity;
    }

    @Override
    public UserIdentity addWechatUser(Integer userId, String unionId) {
        UserIdentity userIdentity = new UserIdentity();
        userIdentity.setUserId(userId);
        userIdentity.setIdentifier(unionId);
        userIdentity.setUserIdentityType(UserIdentityTypeEnum.WECHAT.getValue());
        userIdentity.setIsDelete(DeleteEnum.FALSE.getValue());
        userIdentityMapper.insert(userIdentity);
        return userIdentity;
    }

    @Override
    public String getToken(Integer userId) {
        String token = IdUtil.simpleUUID();
        return authStorage.getOrSetToken(userId.toString(), token);
    }

    @Override
    public Boolean isEmailAndPasswordMatches(String email, String password) {
        UserIdentityCondition condition = new UserIdentityCondition();
        condition.setUserIdentityType(UserIdentityTypeEnum.EMAIL);
        condition.setIdentifier(email);
        List<UserIdentity> userIdentities = queryUserIdentity(condition);
        UserIdentity userIdentity = userIdentities.get(0);
        String salt = userIdentity.getCredentialSalt();
        String encodedPassword = CryptoUtils.md5Encrypt(salt, password);
        return userIdentity.getCredential().equals(encodedPassword);
    }

    @Override
    public Long isForbiddenLogin(String email) {
        return authStorage.hasAuthForbiddenLock(email);
    }

    @Override
    public void processLoginFail(String email) {
        Long counter = authStorage.addAuthFailCounter(email);
        if (counter >= LOGIN_FAIL_THRESHOLD) {
            authStorage.setAuthForbiddenLock(email, LOGIN_FORBIDDEN_MS);
            authStorage.resetAuthFailCounter(email);
        }
    }

    @Override
    public void processLoginSuccess(String email) {
        authStorage.resetAuthFailCounter(email);
    }

    @Override
    public Boolean isForbiddenVerificationValidate(String identity, VerificationEnum verificationEnum) {
        return verificationCodeStorage.getValidateFailCounter(identity, verificationEnum) > VERIFICATION_FAIL_THRESHOLD;
    }

    @Override
    public void processVerificationFail(String identity, VerificationEnum verificationEnum) {
        verificationCodeStorage.addValidateFailCounter(identity, verificationEnum);
    }

    @Override
    public void processVerificationSuccess(String identity, VerificationEnum verificationEnum) {
        verificationCodeStorage.resetValidateFailCounter(identity, verificationEnum);
    }

    @Override
    public void resetPassword(String identity, UserIdentityTypeEnum userIdentityTypeEnum, String password) {
        UserIdentityExample example = new UserIdentityExample();
        UserIdentityExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo(DeleteEnum.FALSE.getValue())
                .andUserIdentityTypeEqualTo(userIdentityTypeEnum.getValue())
                .andIdentifierEqualTo(identity);
        UserIdentity userIdentity = new UserIdentity();
        String salt = RandomUtil.randomString(SALT_LENGTH);
        userIdentity.setCredentialSalt(salt);
        String encodedPassword = CryptoUtils.md5Encrypt(salt, password);
        userIdentity.setCredential(encodedPassword);
        userIdentityMapper.updateByExampleSelective(userIdentity, example);
    }

    @Override
    public Boolean isTokenValid(Integer userId, String token) {
        return authStorage.isTokenValid(userId.toString(), token);
    }

    @Override
    public void deleteUserIdentityByCondition(UserIdentityCondition condition) {
        UserIdentityExample example = buildExampleByCondition(condition);
        UserIdentity userIdentity = new UserIdentity();
        userIdentity.setIsDelete(DeleteEnum.TRUE.getValue());
        userIdentityMapper.updateByExampleSelective(userIdentity, example);
    }

    private UserIdentityExample buildExampleByCondition(UserIdentityCondition condition) {
        UserIdentityExample example = new UserIdentityExample();
        UserIdentityExample.Criteria criteria = example.createCriteria();
        criteria.andIsDeleteEqualTo(DeleteEnum.FALSE.getValue());
        if (condition.getUserIdentityType() != null) {
            criteria.andUserIdentityTypeEqualTo(condition.getUserIdentityType().getValue());
        }
        if (condition.getIdentifier() != null) {
            criteria.andIdentifierEqualTo(condition.getIdentifier());
        }
        if (!CollectionUtils.isEmpty(condition.getUserId())) {
            criteria.andUserIdIn(condition.getUserId());
        }
        if (condition.getCredential() != null) {
            criteria.andCredentialEqualTo(condition.getCredential());
        }
        return example;
    }

}
