package art.meiye.venus.service.validate.annotations;

import art.meiye.venus.service.validate.validators.EmailRegisterValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 校验注解 - 邮箱注册
 * @author Garin
 * @date 2020-08-10
 */
@Constraint(validatedBy = EmailRegisterValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface EmailRegisterValidation {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
