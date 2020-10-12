package art.meiye.venus.dal.redis.auth;

import art.meiye.venus.dal.redis.RedisKeyEnum;

/**
 * Token存储层
 * @author Garin
 * @date 2020-08-11
 */
public interface AuthStorage {

    /**
     * 设置token
     * @param identity 身份
     * @param token 待设置的token
     */
    void setToken(String identity, String token);

    /**
     * userId和token是否匹配
     * @param identity identity
     * @param token 待验证的token
     * @return 是否匹配
     */
    boolean isTokenValid(String identity, String token);

    /**
     * 获取/设置token，存在则获取，不存在则设置
     * @param identity identity
     * @param token token
     * @return token
     */
    String getOrSetToken(String identity, String token);

    /**
     * 获取鉴权失败计数器
     * @param identity 锁定粒度
     * @return 计数值
     */
    Long getAuthFailCounter(String identity);

    /**
     * 增加鉴权失败计数器
     * expire: {@link RedisKeyEnum#AUTH_FAIL_COUNTER}
     * @param identity 锁定粒度
     * @return 计数值
     */
    Long addAuthFailCounter(String identity);

    /**
     * 重置鉴权失败计数器
     * @param identity 锁定粒度
     */
    void resetAuthFailCounter(String identity);

    /**
     * 设置禁用鉴权锁
     * @param identity id
     * @param millisecond 鉴权禁用的毫秒数
     */
    void setAuthForbiddenLock(String identity, Long millisecond);

    /**
     * 查询是否有禁用鉴权锁
     * @param identity id
     * @return 锁剩余过期时间(秒)
     */
    Long hasAuthForbiddenLock(String identity);
}
