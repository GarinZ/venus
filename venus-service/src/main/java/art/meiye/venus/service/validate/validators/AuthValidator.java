package art.meiye.venus.service.validate.validators;

import art.meiye.venus.biz.UserIdentityBizHandle;
import art.meiye.venus.common.api.ErrorMapping;
import art.meiye.venus.common.domain.AppIdEnum;
import art.meiye.venus.service.vo.auth.AuthRequest;
import art.meiye.venus.service.validate.ErrorPayload;
import art.meiye.venus.service.validate.annotations.AuthValidation;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 校验机 - 鉴权
 * @author Garin
 * @date 2020-08-18
 */
public class AuthValidator implements ConstraintValidator<AuthValidation, AuthRequest> {

    @Autowired
    UserIdentityBizHandle userBizHandle;

    /**
     * 鉴权接口错误信息统一采用{@link ErrorMapping.AUTH_FAIL}
     * @param request 请求参数
     * @param context 校验上下文信息
     * @return 是否校验通过
     */
    @Override
    public boolean isValid(AuthRequest request, ConstraintValidatorContext context) {
        HibernateConstraintValidatorContext hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);
        hibernateContext.disableDefaultConstraintViolation();
        // 1. 校验秘钥 || appId || token || userid 是否为空
        if (StringUtils.isEmpty(request.getToken())
                || request.getAppId() == null
                || StringUtils.isEmpty(request.getToken())
                || StringUtils.isEmpty(request.getSecretKey())
                || request.getUserId() == null) {
            ErrorPayload.addErrorToHibernateContextFinal(hibernateContext, "", ErrorMapping.AUTH_FIELD_EMPTY);
            return false;
        }
        // 2. 校验appId合法性
        AppIdEnum appIdEnum = AppIdEnum.getByValue(request.getAppId());
        if (appIdEnum == null) {
            ErrorPayload.addErrorToHibernateContextFinal(hibernateContext, "", ErrorMapping.AUTH_SK_INVALID);
            return false;
        }
        // 3. 校验appId与sk是否匹配
        if (!request.getSecretKey().equals(appIdEnum.getSk())) {
            ErrorPayload.addErrorToHibernateContextFinal(hibernateContext, "", ErrorMapping.AUTH_SK_INVALID);
            return false;
        }
        // 4. 校验userid和token的匹配性
        if (!userBizHandle.isTokenValid(request.getUserId(), request.getToken())) {
            ErrorPayload.addErrorToHibernateContextFinal(hibernateContext, "", ErrorMapping.AUTH_TOKEN_INVALID);
            return false;
        }
        return true;
    }
}
