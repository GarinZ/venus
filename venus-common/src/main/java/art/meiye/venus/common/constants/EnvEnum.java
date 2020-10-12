package art.meiye.venus.common.constants;

/**
 * 执行环境常量
 * @author Garin
 * @date 2020-08-26
 */
public enum EnvEnum {
    /**
     * DEV - 开发环境
     * PRODUCT - 生产环境
     */
    DEV("development"),
    PRODUCT("production"),
    ;

    private final String value;

    EnvEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
