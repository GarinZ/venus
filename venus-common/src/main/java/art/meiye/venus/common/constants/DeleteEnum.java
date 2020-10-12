package art.meiye.venus.common.constants;

/**
 * 删除状态枚举
 * @author Garin
 * @date 2020-08-11
 */
public enum DeleteEnum {
    /** 未删除 */
    FALSE(0),
    /** 已删除 */
    TRUE(1);

    private Integer value;

    DeleteEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }
}
