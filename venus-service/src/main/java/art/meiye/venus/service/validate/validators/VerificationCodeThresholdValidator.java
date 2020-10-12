package art.meiye.venus.service.validate.validators;

import art.meiye.venus.common.api.ErrorMapping;
import art.meiye.venus.common.utils.RegUtils;
import art.meiye.venus.dal.redis.verificationcode.VerificationCodeStorage;
import art.meiye.venus.dal.redis.verificationcode.VerificationEnum;
import art.meiye.venus.service.validate.ErrorPayload;
import art.meiye.venus.service.validate.annotations.VerificationCodeThreshold;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 校验机 - 验证码发送是否过频繁
 * @author Garin
 * @date 2020-08-09
 */
public class VerificationCodeThresholdValidator implements ConstraintValidator<VerificationCodeThreshold, String> {
    @Autowired
    VerificationCodeStorage verificationCodeStorage;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        HibernateConstraintValidatorContext hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);
        hibernateContext.disableDefaultConstraintViolation();
        // 1. 校验email是否为邮箱
        if (!RegUtils.isValidEmail(email)) {
            ErrorPayload.addErrorToHibernateContextFinal(hibernateContext, "email", ErrorMapping.EMAIL_NOT_VALID);
            return false;
        }
        // 2. 校验当前email发送验证码是否过于频繁
        if (!verificationCodeStorage.isVerificationCodeAvailable(email, VerificationEnum.EMAIL)) {
            ErrorPayload.addErrorToHibernateContextFinal(hibernateContext, "email", ErrorMapping.EMAIL_VERIFY_CODE_TOO_OFTEN);
            return false;
        }
        return true;
    }
}
