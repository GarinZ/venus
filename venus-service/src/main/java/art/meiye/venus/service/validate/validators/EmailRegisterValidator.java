package art.meiye.venus.service.validate.validators;

import art.meiye.venus.biz.UserIdentityBizHandle;
import art.meiye.venus.common.api.ErrorMapping;
import art.meiye.venus.common.utils.RegUtils;
import art.meiye.venus.dal.redis.verificationcode.VerificationEnum;
import art.meiye.venus.service.vo.auth.EmailRegisterRequest;
import art.meiye.venus.service.validate.ErrorPayload;
import art.meiye.venus.service.validate.annotations.EmailRegisterValidation;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 校验机 - 邮箱注册
 * @author Garin
 * @date 2020-08-10
 */
public class EmailRegisterValidator implements ConstraintValidator<EmailRegisterValidation, EmailRegisterRequest> {
    @Autowired
    UserIdentityBizHandle userIdentityBizHandle;

    @Override
    public boolean isValid(EmailRegisterRequest request, ConstraintValidatorContext context) {
        HibernateConstraintValidatorContext hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);
        hibernateContext.disableDefaultConstraintViolation();
        // 1. 校验邮箱合法性
        if (StringUtils.isEmpty(request.getEmail()) || !RegUtils.isValidEmail(request.getEmail())) {
            ErrorPayload.addErrorToHibernateContextFinal(hibernateContext, "email", ErrorMapping.EMAIL_NOT_VALID);
            return false;
        }
        // 2. 校验密码合法性
        if (StringUtils.isEmpty(request.getPassword()) || !RegUtils.isValidPassword(request.getPassword())) {
            ErrorPayload.addErrorToHibernateContextFinal(hibernateContext, "password", ErrorMapping.PASSWORD_NOT_VALID);
            return false;
        }
        // 3. 校验验证码是否为空
        if (request.getVerificationCode() == null) {
            ErrorPayload.addErrorToHibernateContextFinal(hibernateContext, "verificationCode", ErrorMapping.VERIFICATION_CODE_EMPTY);
            return false;
        }
        // 4. 验证码错误阈值校验
        if (userIdentityBizHandle.isForbiddenVerificationValidate(request.getEmail(), VerificationEnum.EMAIL)) {
            ErrorPayload.addErrorToHibernateContextFinal(hibernateContext, "verificationCode", ErrorMapping.VERIFICATION_CODE_VALIDATE_FORBIDDEN);
            return false;
        }
        // 5. 校验邮箱是否已经注册
        if (userIdentityBizHandle.isEmailExist(request.getEmail())) {
            ErrorPayload.addErrorToHibernateContextFinal(hibernateContext, "email", ErrorMapping.EMAIL_EXIST);
            return false;
        }
        // 6. 校验邮箱和验证码是否匹配
        if (!userIdentityBizHandle.isEmailAndVerificationCodeMatches(request.getEmail(), request.getVerificationCode())) {
            // 验证码错误计数器增加
            userIdentityBizHandle.processVerificationFail(request.getEmail(), VerificationEnum.EMAIL);
            ErrorPayload.addErrorToHibernateContextFinal(hibernateContext, "verificationCode", ErrorMapping.EMAIL_VERIFICATION_NOT_MATCH);
            return false;
        }
        // 验证码错误计数器清空
        userIdentityBizHandle.processVerificationSuccess(request.getEmail(), VerificationEnum.EMAIL);
        return true;
    }
}
