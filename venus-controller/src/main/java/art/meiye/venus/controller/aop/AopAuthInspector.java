package art.meiye.venus.controller.aop;

import art.meiye.venus.biz.UserIdentityBizHandle;
import art.meiye.venus.common.api.CommonResponse;
import art.meiye.venus.common.domain.CookieEnum;
import art.meiye.venus.common.utils.ThreadContextHolder;
import art.meiye.venus.controller.aop.annotation.NotAuth;
import art.meiye.venus.controller.utils.CookieUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Optional;

/**
 * AOP - 鉴权拦截器
 * @author Garin
 * @date 2020-08-26
 */
@Component
@Aspect
@Slf4j
@Order(2)
public class AopAuthInspector extends BaseInspector {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserIdentityBizHandle userIdentityBizHandle;

    @Around("controller()")
    public Object invokeControllerAround(ProceedingJoinPoint jp) throws Throwable {
        // 1. 根据注解判断是否需要鉴权
        if (isNeedAuth(jp)) {
            Optional<String> userId = CookieUtils.getCookieItemByName(CookieEnum.USER_ID, request.getCookies());
            Optional<String> token = CookieUtils.getCookieItemByName(CookieEnum.TOKEN, request.getCookies());
            // 2. Cookie中没有身份认证信息则返回
            if (!userId.isPresent() || !token.isPresent()) {
                return CommonResponse.identityExpire();
            }
            MDC.put("userId", userId.get());
            // 3. 认证
            boolean isValid = userIdentityBizHandle.isTokenValid(Integer.valueOf(userId.get()), token.get());
            if (!isValid) {
                return CommonResponse.identityExpire();
            }
            // 4. 将userId作为变量注入threadContext
            ThreadContextHolder.setUserId(Integer.valueOf(userId.get()));
        }
        return jp.proceed();
    }

    /**
     * 根据注解判断是否需要鉴权
     * @param jp 切点
     * @return 是否需要鉴权
     */
    private Boolean isNeedAuth(ProceedingJoinPoint jp) {
        MethodSignature methodSignature = (MethodSignature) jp.getSignature();
        Class declaringType = methodSignature.getDeclaringType();
        Method method = methodSignature.getMethod();
        try {
            Annotation classAnnotation = declaringType.getAnnotation(NotAuth.class);
            if (classAnnotation != null) {
                return false;
            }
            NotAuth methodAnnotation = method.getAnnotation(NotAuth.class);
            return methodAnnotation == null;

        } catch (Exception e) {
            return true;
        }
    }

}
