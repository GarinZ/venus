package art.meiye.venus.service.vo.auth;

import art.meiye.venus.service.validate.annotations.AuthValidation;

import lombok.Data;

/**
 * 鉴权请求
 * @author Garin
 * @date 2020-08-14
 */
@Data
@AuthValidation
public class AuthRequest {
    /**
     * userid
     */
    private Integer userId;
    /**
     * 鉴权token
     */
    private String token;
    /**
     * 通信秘钥
     */
    private String secretKey;
    /**
     * 产品线ID
     */
    private Integer appId;
}
