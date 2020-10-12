package art.meiye.venus.common.exceptions;

import art.meiye.venus.common.api.ErrorMapping;
import lombok.Data;

/**
 * 业务异常
 * @author Garin
 * @date 2020-08-27
 */
@Data
public class BizException extends RuntimeException {
    private Integer errorCode;

    private String errorMessage;

    private ErrorMapping errorMapping;

    public BizException() {
    }

    public BizException(ErrorMapping mapping) {
        this.errorCode = mapping.getErrorCode();
        this.errorMessage = mapping.getErrorMsg();
        this.errorMapping = mapping;
    }
}
