package art.meiye.venus.service.validate;

import art.meiye.venus.common.api.ErrorMapping;
import art.meiye.venus.common.constants.Constants;
import lombok.Data;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

/**
 * 作为HibernateConstraintValidatorContext的dynamic payload
 * 记录校验机输出的错误信息
 * @author Garin
 * @date 2020-08-11
 */
@Data
public class ErrorPayload {
    private String errorField;
    private Integer errorCode;
    private String errorMsg;

    ErrorPayload() { }

    ErrorPayload(String errorField, ErrorMapping errorMapping) {
        this.errorField = errorField;
        this.errorCode = errorMapping.getErrorCode();
        this.errorMsg = errorMapping.getErrorMsg();
    }

    ErrorPayload(String errorField, Integer errorCode, String errorMsg) {
        this.errorField = errorField;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    /**
     * 为校验上下文装配错误信息
     * @param context hibernate提供的校验上下文
     * @param errorField 错误的字段字面量
     * @param errorMapping 错误信息枚举
     */
    public static void addErrorToHibernateContext(HibernateConstraintValidatorContext context,
                                                  String errorField, ErrorMapping errorMapping) {
        ErrorPayload errorPayload = new ErrorPayload(errorField, errorMapping);
        context.withDynamicPayload(errorPayload);
    }

    public static void addErrorToHibernateContext(HibernateConstraintValidatorContext context,
                                                  String errorField, Integer errorCode, String errorMsg) {
        ErrorPayload errorPayload = new ErrorPayload(errorField, errorCode, errorMsg);
        context.withDynamicPayload(errorPayload);
    }

    /**
     * 为校验上下文装配错误信息，并完成装配
     * 该方法主要为了减少样板代码
     * @param context hibernate提供的校验上下文
     * @param errorField 错误的字段字面量
     * @param errorMapping 错误信息枚举
     */
    public static void addErrorToHibernateContextFinal(HibernateConstraintValidatorContext context,
                                                       String errorField, ErrorMapping errorMapping) {
        addErrorToHibernateContext(context, errorField, errorMapping);
        context.buildConstraintViolationWithTemplate(Constants.EMPTY_STRING).addConstraintViolation();
    }

    public static void addErrorToHibernateContextFinal(HibernateConstraintValidatorContext context,
                                                       String errorField, Integer errorCode, String errorMsg) {
        addErrorToHibernateContext(context, errorField, errorCode, errorMsg);
        context.buildConstraintViolationWithTemplate(Constants.EMPTY_STRING).addConstraintViolation();
    }
}
