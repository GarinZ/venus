package art.meiye.venus.controller;

import art.meiye.venus.common.api.CommonResponse;
import art.meiye.venus.common.api.ErrorMapping;
import art.meiye.venus.common.exceptions.BizException;
import art.meiye.venus.common.utils.ThreadContextHolder;
import art.meiye.venus.controller.aop.annotation.NotAuth;
import art.meiye.venus.controller.utils.WechatUtils;
import art.meiye.venus.sal.wechat.WechatService;
import art.meiye.venus.service.AccountService;
import art.meiye.venus.service.vo.account.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

/**
 * 控制器 - 账户
 * @author Garin
 * @date 2020-08-26
 */

@Slf4j
@Controller
@RequestMapping("/venus/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Value("${wechat.appId}")
    private String appId;

    @Value("${wechat.stateKey}")
    private String stateKey;

    @Value("${wechat.bindStateToken}")
    private String bindStateToken;

    @Autowired
    WechatService wechatService;

    /**
     * 获取用户信息接口
     * @return 用户信息
     */
    @PostMapping("/get/userProfile")
    @ResponseBody
    public CommonResponse<UserProfileResponse> getUserProfile() {
        Integer userId = ThreadContextHolder.getUserId();
        UserProfileResponse userProfile = accountService.getUserProfile(userId);
        return CommonResponse.success(userProfile);
    }

    /**
     * 更新用户信息（按需更新）
     * @param request 用户信息
     * @return 更新后的信息
     */
    @ResponseBody
    @PostMapping("/update/userProfile")
    public CommonResponse<UserProfileResponse> updateUserProfile(@RequestBody @Valid UserProfileUpdateRequest request) {
        Integer userId = ThreadContextHolder.getUserId();
        UserProfileResponse response = accountService.updateUserProfile(userId, request);
        return CommonResponse.success(response);
    }

    /**
     * 上传头像
     * @param request 头像base64
     * @return 头像url
     * @throws BizException 可能抛出base64解析报错或七牛云上传报错
     */
    @ResponseBody
    @PostMapping("/upload/avatar")
    public CommonResponse<ImageUploadResponse> uploadImage(@RequestBody ImageUploadRequest request) throws BizException {
        Integer userId = ThreadContextHolder.getUserId();
        ImageUploadResponse response = accountService.uploadImageWithBase64(userId, request);
        return CommonResponse.success(response);
    }

    /**
     * 绑定微信
     * @param response HttpServletResponse
     * @throws IOException 重定向异常
     */
    @GetMapping("/bind/wechat")
    public void bindWechat(HttpServletResponse response) throws IOException {
        Integer userId = ThreadContextHolder.getUserId();
        final String redirectUrl = "https://www.meiye.art/venus/account/bind/wechat/callback";
        final String state = WechatUtils.getEncryptState(stateKey, bindStateToken, userId);
        String wechatLoginUrl = wechatService.getWechatLoginUrl(appId, redirectUrl, state);
        response.sendRedirect(wechatLoginUrl);
    }

    /**
     * 绑定微信回调
     * @param code 用户授权code
     * @param state 前链的验证字段
     */
    @NotAuth
    @GetMapping("/bind/wechat/callback")
    public String bindWechat(@RequestParam String code, @RequestParam String state, HttpServletRequest request) {
        try {
            String decryptState = WechatUtils.getDecryptState(stateKey, state);
            request.setAttribute("isSuccess", "0");
            if (!WechatUtils.isDecryptStateValid(decryptState, bindStateToken)) {
                request.setAttribute("result", "登录凭证校验失败，请重新登录");
                return "/wechat-login-feedback.html";
            }
            Integer userId = WechatUtils.getUserIdFromState(decryptState);
            ThreadContextHolder.setUserId(userId);
            accountService.bindWechat(code);
            request.setAttribute("result", "登录成功");
            request.setAttribute("isSuccess", "1");
        } catch (BizException exception) {
            request.setAttribute("result", exception.getErrorMessage());
        } catch (Exception exception) {
            request.setAttribute("result", ErrorMapping.ACCOUNT_WECHAT_BIND_UNEXPECT_ERROR.getErrorMsg());
            log.error("bindWechat unexpect error", exception);
        }
        return "wechat-login-feedback.html";
    }

    /**
     * 解绑微信
     */
    @ResponseBody
    @PostMapping("/unbind/wechat")
    public CommonResponse<Object> unbindWechat() throws BizException {
        accountService.unbindWechat();
        return CommonResponse.success(null);
    }

    /**
     * 绑定邮箱
     * @param request 请求
     * @return 返回
     */
    @ResponseBody
    @PostMapping("/bind/email")
    public CommonResponse<Object> bindEmail(@RequestBody @Valid EmailBindRequest request) {
        Integer userId = ThreadContextHolder.getUserId();
        accountService.bindEmail(userId, request);
        return CommonResponse.success(null);
    }

    /**
     * 解绑邮箱
     */
    @ResponseBody
    @PostMapping("/unbind/email")
    public CommonResponse<Object> unbindEmail() throws BizException {
        accountService.unbindEmail();
        return CommonResponse.success(null);
    }

}
