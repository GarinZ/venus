package art.meiye.venus.service.validate.annotations;

import art.meiye.venus.service.validate.validators.UserProfileUpdateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 校验注解 - 用户账户信息修改
 * @author Garin
 * @date 2020-08-24
 */
@Constraint(validatedBy = UserProfileUpdateValidator.class)
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface UserProfileUpdateValidation {
    String message() default "";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
