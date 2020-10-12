package art.meiye.venus.service.validate.annotations;

import art.meiye.venus.service.validate.validators.VerificationCodeThresholdValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 校验注解 - 验证码发送是否过频繁
 *
 * 作用于发送验证码的字段上，如：email，phone
 *
 * @see VerificationCodeThresholdValidator 校验规则详情见此类
 * @author Garin
 * @date 2020-08-09
 */
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = VerificationCodeThresholdValidator.class)
public @interface VerificationCodeThreshold {
    String message() default "验证码发送过于频繁";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
