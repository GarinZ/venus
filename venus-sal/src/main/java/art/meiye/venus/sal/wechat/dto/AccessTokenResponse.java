package art.meiye.venus.sal.wechat.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 微信获取AccessToken返回
 * @author Garin
 * @date 2020-08-31
 */
@Data
public class AccessTokenResponse {
    /**
     * 接口调用凭证
     */
    @JSONField(name = "access_token")
    private String accessToken;
    /**
     * access_token接口调用凭证超时时间，单位（秒）
     */
    @JSONField(name = "expires_in")
    private Integer expireIn;
    /**
     * 用户刷新access_token
     */
    @JSONField(name = "refresh_token")
    private String refreshToken;
    /**
     * 授权用户唯一标识
     */
    @JSONField(name = "openid")
    private String openId;
    /**
     * 用户授权的作用域，使用逗号（,）分隔
     */
    private String scope;
    /**
     * 	当且仅当该网站应用已获得该用户的userinfo授权时，才会出现该字段。
     */
    @JSONField(name = "unionid")
    private String unionId;
}
