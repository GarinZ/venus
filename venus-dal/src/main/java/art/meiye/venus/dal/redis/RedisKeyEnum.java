package art.meiye.venus.dal.redis;

import art.meiye.venus.common.constants.Constants;

import java.util.concurrent.TimeUnit;

/**
 * Redis存储的前缀枚举
 * @author Garin
 * @date 2020-08-09
 */
public enum RedisKeyEnum {

    /** 邮箱验证码 - 过期时间稍长于告知用户时间 */
    EMAIL_CODE("emailCode:", TimeUnit.MINUTES.toMillis(6)),
    /** 邮箱验证码发送频率锁 - 锁时间稍短于告知用户时间 */
    EMAIL_CODE_LOCK("emailCodeLock:", TimeUnit.SECONDS.toMillis(50)),
    /** 邮箱验证码校验失败次数 - 记录邮箱验证码校验失败次数 */
    EMAIL_CODE_VALIDATE_FAIL_COUNTER("emailCodeValidateFailCounter:", TimeUnit.HOURS.toMillis(12)),
    /** 用户登录token */
    USER_TOKEN("userLoginToken:", TimeUnit.DAYS.toMillis(Constants.TOKEN_EXPIRE_DAY)),
    /** 鉴权失败计数器 - 记录登录失败次数 */
    AUTH_FAIL_COUNTER("authFailCounter:", TimeUnit.DAYS.toMillis(1)),
    /** 鉴权禁用锁 - 加锁则进制登录重试 */
    AUTH_FORBIDDEN_LOCK("authFailLock:", TimeUnit.MINUTES.toMillis(15)),
    /** 密码重置验证码 - 过期时间稍长于告知用户时间 */
    PASSWORD_RESET_CODE("emailCode:", TimeUnit.MINUTES.toMillis(6)),
    /** 密码重置验证码发送频率锁 - 锁时间稍短于告知用户时间 */
    PASSWORD_RESET_CODE_LOCK("emailCodeLock:", TimeUnit.SECONDS.toMillis(50)),
    /** 密码重置验证码校验失败次数 - 记录邮箱验证码校验失败次数 */
    PASSWORD_RESET_CODE_VALIDATE_FAIL_COUNTER("passwordCodeValidateFailCounter:", TimeUnit.HOURS.toMillis(12)),

    ;

    /**
     * redis中key的存储前缀
     */
    private final String prefix;
    /**
     * redis中key的存储过期时间(毫秒)
     */
    private final Long expire;

    RedisKeyEnum(String prefix, Long expire) {
        this.prefix = prefix;
        this.expire = expire;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public Long getExpire() {
        return this.expire;
    }
}
