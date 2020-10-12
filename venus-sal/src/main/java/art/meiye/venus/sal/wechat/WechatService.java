package art.meiye.venus.sal.wechat;

import art.meiye.venus.common.api.enums.MetaStatus;
import art.meiye.venus.sal.wechat.dto.AccessTokenResponse;
import art.meiye.venus.sal.wechat.dto.ErrorResponse;
import art.meiye.venus.sal.wechat.dto.UserProfileQueryResponse;
import cn.hutool.core.util.URLUtil;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * 微信服务
 * @author Garin
 * @date 2020-08-31
 */
@Service
@Slf4j
public class WechatService {

    @Autowired
    OkHttpClient httpClient;

    /**
     * 获取微信登录URL
     * @param appId 微信appId
     * @param redirectUrl 重定向URL（不需要进行encode）
     * @param state 加密后的state
     * @return 拼接完成的微信登录URL
     */
    public String getWechatLoginUrl(String appId, String redirectUrl, String state) {
        return "https://open.weixin.qq.com/connect/qrconnect?" +
                "appid=" + appId +
                "&redirect_uri=" + URLUtil.encodeQuery(redirectUrl) +
                "&response_type=code" +
                "&scope=snsapi_login" +
                "&state=" + state + "#wechat_redirect";
    }

    /**
     * 通过code获取access_token
     * @param appId 应用唯一标识，在微信开放平台提交应用审核通过后获得
     * @param secretId 应用密钥AppSecret，在微信开放平台提交应用审核通过后获得
     * @param code 填写第一步获取的code参数
     * @return 用户个人信息；状态码为非200返回null
     */
    public Optional<AccessTokenResponse> getAccessToken(String appId, String secretId, String code) {
        final String baseUrl = "https://api.weixin.qq.com/sns/oauth2/access_token";
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(baseUrl)).newBuilder();
        urlBuilder.addQueryParameter("appid", appId);
        urlBuilder.addQueryParameter("secret", secretId);
        urlBuilder.addQueryParameter("code", code);
        urlBuilder.addQueryParameter("grant_type", "authorization_code");
        Request request = new Request.Builder().url(urlBuilder.build().toString()).build();
        Call call = httpClient.newCall(request);
        Response response;
        try {
            response = call.execute();
            if (!MetaStatus.SUCCESS.getCode().equals(response.code())) {
                log.error("请求微信accessToken失败\t状态码异常\t{}", response);
                return Optional.empty();
            }
            String bodyStr = Objects.requireNonNull(response.body()).string();
            ErrorResponse errorResponse = JSON.parseObject(bodyStr, ErrorResponse.class);
            if (errorResponse.getErrorCode() != null) {
                log.error("请求微信accessToken失败\t请求错误\t{}", response);
                return Optional.empty();
            }
            return Optional.of(JSON.parseObject(bodyStr, AccessTokenResponse.class));
        } catch (IOException e) {
            log.error("请求微信accessToken失败\tI/O异常", e);
            return Optional.empty();
        }
    }

    /**
     * 获取用户个人信息（UnionID机制）
     * 此接口用于获取用户个人信息。开发者可通过OpenID来获取用户基本信息。
     * 特别需要注意的是，如果开发者拥有多个移动应用、网站应用和公众帐号，可通过获取用户基本信息中的unionid来区分用户的唯一性，
     * 因为只要是同一个微信开放平台帐号下的移动应用、网站应用和公众帐号，用户的unionid是唯一的。换句话说，同一用户，对同一个
     * 微信开放平台下的不同应用，unionid是相同的。请注意，在用户修改微信头像后，旧的微信头像URL将会失效，因此开发者应该自己
     * 在获取用户信息后，将头像图片保存下来，避免微信头像URL失效后的异常情况。
     *
     * @param accessToken 调用凭证
     * @param openId 普通用户的标识，对当前开发者帐号唯一
     * @return 用户个人信息；状态码为非200返回null
     */
    public Optional<UserProfileQueryResponse> getUserProfile(String accessToken, String openId) {
        final String baseUrl = "https://api.weixin.qq.com/sns/userinfo";
        HttpUrl.Builder urlBuilder = Objects.requireNonNull(HttpUrl.parse(baseUrl)).newBuilder();
        urlBuilder.addQueryParameter("access_token", accessToken);
        urlBuilder.addQueryParameter("openid", openId);
        Request request = new Request.Builder().url(urlBuilder.build().toString()).build();
        Call call = httpClient.newCall(request);
        Response response;
        try {
            response = call.execute();
            if (!MetaStatus.SUCCESS.getCode().equals(response.code())) {
                log.error("请求微信用户个人信息失败\t状态码异常\t{}", response);
                return Optional.empty();
            }
            String bodyStr = Objects.requireNonNull(response.body()).string();
            ErrorResponse errorResponse = JSON.parseObject(bodyStr, ErrorResponse.class);
            if (errorResponse.getErrorCode() != null) {
                log.error("请求微信用户个人信息失败\t请求错误\t{}", response);
                return Optional.empty();
            }
            return Optional.of(JSON.parseObject(bodyStr, UserProfileQueryResponse.class));
        } catch (IOException e) {
            log.error("请求微信accessToken失败\tI/O异常", e);
            return Optional.empty();
        }
    }
}
