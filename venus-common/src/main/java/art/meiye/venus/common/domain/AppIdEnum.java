package art.meiye.venus.common.domain;

import java.util.Arrays;

/**
 * 应用id枚举
 * @author Garin
 * @date 2020-08-18
 */
public enum  AppIdEnum {
    /**
     * appId枚举
     */
    UX_MAP(0, "http://www.uxmap.cn/page/#/uxmap/index", "ebfa1842e2bd49f4806d671771df4fff"),
    MEI_YE(1, "https://www.meiye.art/", "6118e8d83b8b499087b09653b13cb2fd"),
    MEI_YE_PRO(2, "", "f44542530ffd415998d90aa6cd6eee37"),
    WORK_COLLECTION(3, "", "d2a6b84e157d489aabd055fa5ab27f55"),
    ;


    AppIdEnum(Integer value, String redirectTarget, String sk) {
        this.value = value;
        this.redirectTarget = redirectTarget;
        this.sk = sk;
    }

    /**
     * 枚举值
     */
    private final Integer value;
    /**
     * 登录成功的重定向链接
     */
    private final String redirectTarget;
    /**
     * 秘钥secretKey
     */
    private final String sk;

    public Integer getValue() {
        return value;
    }

    public String getRedirectTarget() {
        return redirectTarget;
    }

    public String getSk() {
        return sk;
    }

    /**
     * 值是否合法
     * @param value 值
     * @return 是否合法
     */
    public static Boolean isValid(Integer value) {
        if (value == null) {
            return false;
        }
        return Arrays.stream(AppIdEnum.values()).anyMatch(item -> item.getValue().equals(value));
    }

    /**
     * 根据值获取value
     * @param value 枚举值
     * @return AppIdEnum
     */
    public static AppIdEnum getByValue(Integer value) {
        for (AppIdEnum item : AppIdEnum.values()) {
            if (item.getValue().equals(value)) {
                return item;
            }
        }
        return null;
    }

}
