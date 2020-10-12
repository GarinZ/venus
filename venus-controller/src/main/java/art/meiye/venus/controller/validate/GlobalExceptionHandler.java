package art.meiye.venus.controller.validate;

import art.meiye.venus.common.api.CommonResponse;
import art.meiye.venus.common.api.ErrorInfo;
import art.meiye.venus.common.exceptions.BizException;
import art.meiye.venus.service.validate.ErrorPayload;
import org.hibernate.validator.engine.HibernateConstraintViolation;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Controller 全局异常处理
 * @author Garin
 * @date 2020-08-07
 */

@ControllerAdvice(basePackages = "art.meiye.venus.controller")
public class GlobalExceptionHandler {
    /**
     * 参数校验异常处理
     * @param ex 参数校验异常
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({MethodArgumentNotValidException.class})
    public CommonResponse<Object> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorInfo> errors = ex.getBindingResult().getAllErrors().stream().map((error) -> {
            HibernateConstraintViolation<?> violation = error.unwrap(HibernateConstraintViolation.class);
            ErrorPayload errorPayload = violation.getDynamicPayload(ErrorPayload.class);
            return new ErrorInfo(errorPayload.getErrorCode(), errorPayload.getErrorMsg());
        }).collect(Collectors.toList());
        return CommonResponse.paramError(errors);
    }

    /**
     * 业务异常处理
     * @param bizException 业务异常
     * @return response
     */
    @ResponseBody
    @ExceptionHandler({BizException.class})
    public CommonResponse<Object> handleValidationExceptions(BizException bizException) {
        return CommonResponse.bizError(bizException);
    }
}
