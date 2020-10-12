package art.meiye.venus.common.api.enums;

/**
 * RESTFUL接口 - 请求状态码
 * @author Garin
 * @date 2020-08-07
 */
public enum MetaStatus {
    /** 成功 */
    SUCCESS(200),
    /** 权限校验错误 */
    AUTH_ERROR(300),
    /** 参数校验错误 */
    PARAM_ERROR(400),
    /** 参数内部 */
    SYSTEM_ERROR(500);

    private Integer code;

    MetaStatus(Integer code) {
        this.code = code;
    }

    public Integer getCode() {
        return this.code;
    }
}
