package art.meiye.venus.common.domain;

/**
 * 用户身份类型枚举
 * @author Garin
 * @date 2020-08-11
 */
public enum UserIdentityTypeEnum {
    /** 微信账户 */
    WECHAT(0),
    /** 邮箱账户 */
    EMAIL(1);

    private Integer value;

    UserIdentityTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }
}
