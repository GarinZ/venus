package art.meiye.venus.dal.redis;

/**
 * Redis返回值枚举
 * @author Garin
 * @date 2020-08-14
 */
public enum  RedisResponseEnum {
    /**
     * 获取过期时间返回
     * EXPIRE_PERMANENT - 永不过期
     * EXPIRE_EMPTY - Key不存在
     */
    EXPIRE_PERMANENT("-1"),
    EXPIRE_EMPTY("-2"),
    ;

    RedisResponseEnum(String value) {
        this.value = value;
    }

    private final String value;

    public String getValue() {
        return value;
    }
}
