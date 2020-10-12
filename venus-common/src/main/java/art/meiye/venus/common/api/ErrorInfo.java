package art.meiye.venus.common.api;

import lombok.Data;

/**
 * RESTFUL接口 - 错误信息
 * @author Garin
 * @date 2020-08-06
 */
@Data
public class ErrorInfo {
    private String errorMessage;
    private Integer errorCode;

    public ErrorInfo() {}

    public ErrorInfo(Integer errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public ErrorInfo(ErrorMapping errorMapping) {
        this.errorCode = errorMapping.getErrorCode();
        this.errorMessage = errorMapping.getErrorMsg();
    }
}
