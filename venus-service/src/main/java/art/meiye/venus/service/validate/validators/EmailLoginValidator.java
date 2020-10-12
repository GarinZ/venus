package art.meiye.venus.service.validate.validators;

import art.meiye.venus.biz.UserIdentityBizHandle;
import art.meiye.venus.common.api.ErrorMapping;
import art.meiye.venus.common.utils.RegUtils;
import art.meiye.venus.dal.redis.RedisResponseEnum;
import art.meiye.venus.service.vo.auth.EmailLoginRequest;
import art.meiye.venus.service.validate.ErrorPayload;
import art.meiye.venus.service.validate.annotations.EmailLoginValidation;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 校验机 - 邮箱登录
 * @author Garin
 * @date 2020-08-10
 */
public class EmailLoginValidator implements ConstraintValidator<EmailLoginValidation, EmailLoginRequest> {

    @Autowired
    UserIdentityBizHandle userIdentityBizHandle;

    @Override
    public boolean isValid(EmailLoginRequest request, ConstraintValidatorContext context) {
        HibernateConstraintValidatorContext hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);
        hibernateContext.disableDefaultConstraintViolation();
        // 1. 校验邮箱合法性
        if (StringUtils.isEmpty(request.getPassword()) || !RegUtils.isValidEmail(request.getEmail())) {
            ErrorPayload.addErrorToHibernateContextFinal(hibernateContext, "email", ErrorMapping.EMAIL_NOT_VALID);
            return false;
        }
        // 2. 校验密码是否为空
        if (StringUtils.isEmpty(request.getPassword())) {
            ErrorPayload.addErrorToHibernateContextFinal(hibernateContext, "password", ErrorMapping.EMAIL_NOT_VALID);
            return false;
        }
        // 3. 校验登录失败次数是否超过阈值
        Long forbiddenSeconds = userIdentityBizHandle.isForbiddenLogin(request.getEmail());
        if (forbiddenSeconds > Long.parseLong(RedisResponseEnum.EXPIRE_EMPTY.getValue())) {
            if (forbiddenSeconds.equals(Long.parseLong(RedisResponseEnum.EXPIRE_PERMANENT.getValue()))) {
                // 永久禁止登录
                ErrorPayload.addErrorToHibernateContextFinal(hibernateContext, "password", ErrorMapping.LOGIN_FORBIDDEN_PERMANENT);
            } else {
                // 暂时禁止登录尝试
                Integer errorCode = ErrorMapping.LOGIN_ATTEMPT_FORBIDDEN.getErrorCode();
                String errorMsg = ErrorMapping.LOGIN_ATTEMPT_FORBIDDEN.getErrorMsg() + "，请" + forbiddenSeconds + "秒后重试";
                ErrorPayload.addErrorToHibernateContextFinal(hibernateContext, "password", errorCode, errorMsg);
            }
            return false;
        }
        // 4. 校验邮箱是否注册
        if (!userIdentityBizHandle.isEmailExist(request.getEmail())) {
            ErrorPayload.addErrorToHibernateContextFinal(hibernateContext, "email", ErrorMapping.EMAIL_NOT_EXIST);
            return false;
        }
        // 5. 校验邮箱和密码是否匹配
        if (!userIdentityBizHandle.isEmailAndPasswordMatches(request.getEmail(), request.getPassword())) {
            // 增加失败校验计数器
            userIdentityBizHandle.processLoginFail(request.getEmail());
            ErrorPayload.addErrorToHibernateContextFinal(hibernateContext, "email", ErrorMapping.IDENTITY_CREDENTIAL_NOT_MATCHES);
            return false;
        }
        // 清空失败校验计数器
        userIdentityBizHandle.processLoginSuccess(request.getEmail());
        return true;
    }
}
