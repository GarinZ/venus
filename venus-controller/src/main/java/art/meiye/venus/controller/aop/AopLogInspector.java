package art.meiye.venus.controller.aop;

import art.meiye.venus.common.api.CommonResponse;
import art.meiye.venus.common.api.enums.MetaStatus;
import art.meiye.venus.common.utils.IpUtils;
import art.meiye.venus.common.utils.ThreadContextHolder;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

/**
 * 日志 & 全局变量 AOP
 * @author Garin
 * @date 2020-08-26
 */
@Component
@Aspect
@Slf4j
@Order(1)
public class AopLogInspector extends BaseInspector {
    /**
     * 产品线Id
     */
    private final static String APP_ID = "appId";
    /**
     * 请求traceId
     */
    private final static String REQ_ID = "reqId";

    @Around("controller()")
    public Object invokeControllerAround(ProceedingJoinPoint jp) throws Throwable {
        ServletRequestAttributes servletRequestAttributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest httpServletRequest = servletRequestAttributes.getRequest();
        HashMap<String, String> headers = new HashMap<>();
        headers.put(HttpHeaders.ACCEPT, httpServletRequest.getHeader(HttpHeaders.ACCEPT));
        headers.put(HttpHeaders.ACCEPT_ENCODING, httpServletRequest.getHeader(HttpHeaders.ACCEPT_ENCODING));
        headers.put(HttpHeaders.ACCEPT_LANGUAGE, httpServletRequest.getHeader(HttpHeaders.ACCEPT_LANGUAGE));
        headers.put(HttpHeaders.AUTHORIZATION, httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION));
        headers.put(HttpHeaders.CONTENT_TYPE, httpServletRequest.getHeader(HttpHeaders.CONTENT_TYPE));
        headers.put(HttpHeaders.COOKIE, httpServletRequest.getHeader(HttpHeaders.COOKIE));
        headers.put(HttpHeaders.ORIGIN, httpServletRequest.getHeader(HttpHeaders.ORIGIN));
        headers.put(HttpHeaders.REFERER, httpServletRequest.getHeader(HttpHeaders.REFERER));
        headers.put(HttpHeaders.USER_AGENT, httpServletRequest.getHeader(HttpHeaders.USER_AGENT));
        String remoteAddress = IpUtils.getIpAddress(httpServletRequest);
        String consumerIdStr = httpServletRequest.getParameter(APP_ID);
        String className = jp.getSignature().toShortString();
        String requestUrl = httpServletRequest.getRequestURI();
        Class returnType = ((MethodSignature) jp.getSignature()).getReturnType();
        String reqId = !StringUtils.isEmpty(httpServletRequest.getParameter(REQ_ID))
                ? httpServletRequest.getParameter(REQ_ID) : UUID.randomUUID().toString();
        ThreadContextHolder.setHeaders(headers);
        ThreadContextHolder.setRequestId(reqId);
        Object je = null;
        MDC.put(REQ_ID, reqId);
        MDC.put("ip", remoteAddress);
        MDC.put("method", className);
        MDC.put("appId", consumerIdStr);
        MDC.put("URL", requestUrl);
        long startTime = System.currentTimeMillis();
        Object[] args = null;
        try {
            args = jp.getArgs();
            je = jp.proceed();

        } finally {
            long endTime = System.currentTimeMillis();
            String argJson = "{}";
            String resJson = "{}";
            int status = MetaStatus.SYSTEM_ERROR.getCode();
            try {
                argJson = JSON.toJSONString(args);
                resJson = JSON.toJSONString(je);
            } catch (Exception e) {
                // do nothing
            }
            if (je != null) {
                Optional<CommonResponse> commonResponseOpt = getCommonResponseFromJe(je);
                if (commonResponseOpt.isPresent()) {
                    CommonResponse commonResponse = commonResponseOpt.get();
                    status = commonResponse.getStatus();
                } else {
                    status = MetaStatus.SUCCESS.getCode();
                }
            }

            log.info("\tparams:{}\tresponse:{}\tstartTime:{}\tendTime:{}\tusing:{}ms\tstatus:{}\tconsumerId:{}\turl:{}",
                    argJson, resJson, startTime, endTime, endTime - startTime, status, consumerIdStr, requestUrl);
            MDC.clear();
            ThreadContextHolder.clean();
        }
        return je;
    }

    private static Optional<CommonResponse> getCommonResponseFromJe(Object je) {
        if (je instanceof CommonResponse) {
            return Optional.ofNullable((CommonResponse) je);
        }
        return Optional.empty();
    }
}
