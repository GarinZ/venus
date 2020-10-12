package art.meiye.venus.dal.redis.verificationcode;

/**
 * 验证码存储
 * @author Garin
 * @date 2020-08-09
 */
public interface VerificationCodeStorage {
    /**
     * 获取验证码
     * @param identify 身份
     * @param verificationEnum 验证码枚举
     * @return 验证码
     */
    String getVerificationCode(String identify, VerificationEnum verificationEnum);

    /**
     * 设置验证码
     * @param identify 身份
     * @param verificationCode 验证码
     * @param verificationEnum 验证码枚举
     * @return 验证码
     */
    String setVerificationCode(String identify, String verificationCode, VerificationEnum verificationEnum);

    /**
     * 校验验证码
     * @param identify 身份
     * @param verificationEnum 验证码枚举
     * @return 是否合法
     */
    Boolean isVerificationCodeMatches(String identify, String code, VerificationEnum verificationEnum);

    /**
     * 设置验证码锁
     * @param identify 身份
     * @return 加锁是否成功
     */
    Boolean lockVerificationCode(String identify, VerificationEnum verificationEnum);

    /**
     * 校验验证码是否发送过于频繁
     * @param identify 身份
     * @param verificationEnum 验证枚举
     * @return 是否可用
     */
    Boolean isVerificationCodeAvailable(String identify, VerificationEnum verificationEnum);

    /**
     * 增加验证失败计数值
     * @param identity id
     * @param verificationEnum 验证码枚举
     * @return 计数值
     */
    Long addValidateFailCounter(String identity, VerificationEnum verificationEnum);

    /**
     * 获取验证码失败计数值
     * @param identity id
     * @param verificationEnum 验证码枚举
     * @return 计数值
     */
    Long getValidateFailCounter(String identity, VerificationEnum verificationEnum);

    /**
     * 重置验证码失败计数值
     * @param identity id
     * @param verificationEnum 验证码枚举
     */
    void resetValidateFailCounter(String identity, VerificationEnum verificationEnum);
}
