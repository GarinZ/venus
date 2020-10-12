package art.meiye.venus.dal.redis.verificationcode;

import art.meiye.venus.dal.redis.RedisKeyEnum;

/**
 * @author Garin
 * @date 2020-08-11
 */
public enum  VerificationEnum {
    /** 邮箱验证码 */
    EMAIL(RedisKeyEnum.EMAIL_CODE, RedisKeyEnum.EMAIL_CODE_LOCK, RedisKeyEnum.EMAIL_CODE_VALIDATE_FAIL_COUNTER),
    /** 密码重置验证码 */
    PASSWORD_RESET(RedisKeyEnum.PASSWORD_RESET_CODE, RedisKeyEnum.PASSWORD_RESET_CODE_LOCK, RedisKeyEnum.PASSWORD_RESET_CODE_VALIDATE_FAIL_COUNTER),
    ;

    VerificationEnum(RedisKeyEnum verificationCode, RedisKeyEnum codeResetLock, RedisKeyEnum validateCounter) {
        this.verificationCode = verificationCode;
        this.codeResetLock = codeResetLock;
        this.validateCounter = validateCounter;
    }

    private final RedisKeyEnum verificationCode;
    private final RedisKeyEnum codeResetLock;
    private final RedisKeyEnum validateCounter;

    public RedisKeyEnum getCodeResetLock() {
        return codeResetLock;
    }

    public RedisKeyEnum getVerificationCode() {
        return verificationCode;
    }

    public RedisKeyEnum getValidateCounter() {
        return validateCounter;
    }
}
