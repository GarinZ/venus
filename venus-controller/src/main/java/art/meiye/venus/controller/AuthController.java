package art.meiye.venus.controller;

import art.meiye.venus.common.api.CommonResponse;
import art.meiye.venus.common.api.ErrorMapping;
import art.meiye.venus.common.exceptions.BizException;
import art.meiye.venus.controller.aop.annotation.NotAuth;
import art.meiye.venus.controller.utils.CookieUtils;
import art.meiye.venus.controller.utils.WechatUtils;
import art.meiye.venus.sal.wechat.WechatService;
import art.meiye.venus.service.AuthService;
import art.meiye.venus.service.dto.UserIdentityDTO;
import art.meiye.venus.service.vo.auth.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * 控制器 - 认证授权
 * @author Garin
 * @date 2020-08-07
 */

@NotAuth
@Controller
@RequestMapping("/venus")
@Validated
@Slf4j
public class AuthController {

    @Autowired
    AuthService authService;

    @Autowired
    CookieUtils cookieUtils;

    @Value("${wechat.appId}")
    private String appId;

    @Value("${wechat.stateKey}")
    private String stateKey;

    @Value("${wechat.loginStateToken}")
    private String loginStateToken;

    @Autowired
    WechatService wechatService;

    /**
     * 发送邮箱验证码接口
     *
     * @param request 请求
     * @return null
     */
    @ResponseBody
    @PostMapping("/login/sendVerificationEmail")
    public CommonResponse<Object> sendVerificationEmail(@RequestBody @Valid VerificationCodeSendRequest request) {
        authService.sendVerificationEmail(request);
        return CommonResponse.success(null);
    }

    /**
     * 邮箱注册接口，注册成功直接获取登录token
     * @param request 请求
     * @param httpServletRequest httpServletRequest
     * @param httpServletResponse httpServletResponse
     * @return userId和token信息
     */
    @ResponseBody
    @PostMapping("/register/email")
    public CommonResponse<EmailRegisterResponse> registerWithEmail(@RequestBody @Valid EmailRegisterRequest request,
                                                                   HttpServletRequest httpServletRequest,
                                                                   HttpServletResponse httpServletResponse) {
        EmailRegisterResponse response = authService.emailRegister(request);
        // 设置cookie
        List<Cookie> cookies = cookieUtils.newCookieInstanceList(response.getUserId(), response.getToken());
        cookies.forEach(httpServletResponse::addCookie);
        return CommonResponse.success(response);
    }

    /**
     * 邮箱登录接口
     * @param request 请求参数
     * @return userId & token
     */
    @ResponseBody
    @PostMapping("/login/email")
    public CommonResponse<EmailLoginResponse> loginWithEmail(@RequestBody @Valid EmailLoginRequest request, HttpServletResponse httpServletResponse) {
        EmailLoginResponse response = authService.emailLogin(request);
        // 设置cookie
        List<Cookie> cookies = cookieUtils.newCookieInstanceList(response.getUserId(), response.getToken());
        cookies.forEach(httpServletResponse::addCookie);
        return CommonResponse.success(response);
    }

    /**
     * 邮箱密码重置接口
     * @param request 请求参数
     * @return null
     */
    @ResponseBody
    @PostMapping("/login/email/passwordReset")
    public CommonResponse<Object> emailPasswordReset (@RequestBody @Valid PasswordResetRequest request) {
        authService.emailPasswordReset(request);
        return CommonResponse.success(null);
    }

    /**
     * 权限校验接口
     * @param request 请求
     * @return 返回
     */
    @ResponseBody
    @PostMapping("/auth")
    public CommonResponse<Object> auth(@RequestBody @Valid AuthRequest request) {
        return CommonResponse.success(null);
    }

    @GetMapping("/login/wechat")
    public void wechatLogin(@RequestParam String url, HttpServletResponse response) throws IOException {
        final String redirectUrl = "https://www.meiye.art/venus/login/wechat/callback";
        final String state = WechatUtils.getEncryptState(stateKey, loginStateToken, url);
        String wechatLoginUrl = wechatService.getWechatLoginUrl(appId, redirectUrl, state);
        response.sendRedirect(wechatLoginUrl);
    }

    /**
     * 微信登录回调
     * @param code 微信返回的
     * @param state 验证码
     * @param request HttpServletRequest
     */
    @GetMapping("/login/wechat/callback")
    public void wechatLoginCallback(@RequestParam String code, @RequestParam String state,
                                      HttpServletRequest request, HttpServletResponse response) {
        try {
            String decryptState = WechatUtils.getDecryptState(stateKey, state);
            request.setAttribute("isSuccess", "0");
            if (!WechatUtils.isDecryptStateValid(decryptState, loginStateToken)) {
                // TODO 重定向到登录失败的页面
            }
            UserIdentityDTO dto = authService.wechatLogin(code);
            List<Cookie> cookies = cookieUtils.newCookieInstanceList(dto.getUserId(), dto.getToken());
            cookies.forEach(response::addCookie);
            // TODO 校验URL合法性
            String url = WechatUtils.getReferrerFromState(decryptState);
            if (StringUtils.isEmpty(url)) {
                // TODO 设置默认跳转链接
                response.sendRedirect("https://meiye.art/#/account/info");
            } else {
                response.sendRedirect(url);
            }
        } catch (BizException exception) {
            // TODO 重定向到登录失败的页面
            request.setAttribute("result", exception.getErrorMessage());
        } catch (Exception exception) {
            // TODO 重定向到登录失败的页面
            request.setAttribute("result", ErrorMapping.ACCOUNT_WECHAT_LOGIN_ERROR.getErrorMsg());
            log.error("wechat login unexpect error", exception);
        }
    }

    /**
     * 退出登录
     */
    @ResponseBody
    @GetMapping("/logout")
    public CommonResponse<Object> logout(HttpServletResponse response) {
        List<Cookie> cookies = cookieUtils.clearCookie();
        cookies.forEach(response::addCookie);
        return CommonResponse.success(null);
    }
}
