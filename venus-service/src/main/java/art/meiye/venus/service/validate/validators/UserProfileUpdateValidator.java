package art.meiye.venus.service.validate.validators;

import art.meiye.venus.biz.UserIdentityBizHandle;
import art.meiye.venus.common.api.ErrorMapping;
import art.meiye.venus.common.utils.RegUtils;
import art.meiye.venus.service.validate.ErrorPayload;
import art.meiye.venus.service.validate.annotations.UserProfileUpdateValidation;
import art.meiye.venus.service.vo.account.UserProfileUpdateRequest;
import cn.hutool.core.util.URLUtil;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * 校验机 - 账户更新
 * @author Garin
 * @date 2020-08-10
 */
public class UserProfileUpdateValidator implements ConstraintValidator<UserProfileUpdateValidation, UserProfileUpdateRequest> {

    @Autowired
    UserIdentityBizHandle userIdentityBizHandle;

    @Override
    public boolean isValid(UserProfileUpdateRequest request, ConstraintValidatorContext context) {
        HibernateConstraintValidatorContext hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);
        hibernateContext.disableDefaultConstraintViolation();
        // 1. 校验用户名合法性(optional)
        if (!StringUtils.isEmpty(request.getUserName()) && !RegUtils.isValidUserName(request.getUserName())) {
            ErrorPayload.addErrorToHibernateContextFinal(hibernateContext, "userName", ErrorMapping.ACCOUNT_USER_NAME_ERROR);
            return false;
        }
        // 2. 校验头像合法性(optional)
        if (!StringUtils.isEmpty(request.getAvatarUrl()) && URLUtil.url(request.getAvatarUrl()) == null) {
            ErrorPayload.addErrorToHibernateContextFinal(hibernateContext, "avatarUrl", ErrorMapping.ACCOUNT_AVATAR_URL_ERROR);
            return false;
        }
        return true;
    }
}
