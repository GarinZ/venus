package art.meiye.venus.common.api;

import art.meiye.venus.common.api.enums.MetaStatus;
import art.meiye.venus.common.exceptions.BizException;
import lombok.Data;

import java.util.Collections;
import java.util.List;

/**
 * RESTFUL接口 - 返回包装类
 * @author Garin
 * @date 2020-08-06
 */
@Data
public class CommonResponse<T> {
    /**
     * 状态码
     * @see MetaStatus 状态码枚举值
     */
    private Integer status;
    /**
     * 数据体(payload)
     */
    private T data;
    /**
     * 当且仅当状态非{@link MetaStatus#SUCCESS}，错误信息有值
     */
    private List<ErrorInfo> errors;

    /**
     * 成功请求封装方法
     * @param data 返回的payload
     * @param <T> payload的数据类型
     * @return 返回
     */
    public static <T> CommonResponse<T> success(T data) {
        CommonResponse<T> response = new CommonResponse<>();
        response.setStatus(MetaStatus.SUCCESS.getCode());
        response.setData(data);
        response.setErrors(null);
        return response;
    }

    /**
     * 参数错误返回封装方法
     * @param errors 错误信息
     * @return 返回
     */
    public static CommonResponse<Object> paramError(List<ErrorInfo> errors) {
        CommonResponse<Object> response = new CommonResponse<>();
        response.setStatus(MetaStatus.PARAM_ERROR.getCode());
        response.setData(null);
        response.setErrors(errors);
        return response;
    }

    /**
     * 封装身份信息失效/过期/不存在
     * @return response
     */
    public static CommonResponse<Object> identityExpire() {
        CommonResponse<Object> response = new CommonResponse<>();
        response.setStatus(MetaStatus.AUTH_ERROR.getCode());
        response.setData(null);
        ErrorInfo errorInfo = new ErrorInfo(ErrorMapping.IDENTITY_EXPIRE);
        response.setErrors(Collections.singletonList(errorInfo));
        return response;
    }

    /**
     * 业务异常返回
     * @return response
     */
    public static CommonResponse<Object> bizError(BizException bizException) {
        CommonResponse<Object> response = new CommonResponse<>();
        response.setStatus(MetaStatus.SYSTEM_ERROR.getCode());
        response.setData(null);
        ErrorInfo errorInfo = null;
        if (bizException.getErrorMapping() != null) {
            errorInfo = new ErrorInfo(bizException.getErrorMapping());
        } else {
            errorInfo = new ErrorInfo(bizException.getErrorCode(), bizException.getErrorMessage());
        }
        response.setErrors(Collections.singletonList(errorInfo));
        return response;
    }
}
