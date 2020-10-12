package art.meiye.venus.service.vo.auth;

import art.meiye.venus.service.validate.annotations.EmailLoginValidation;
import lombok.Data;

/**
 * 邮箱登录请求Vo
 *
 * 校验规则：
 * 1. 邮箱合法
 * 2. 登录重试次数不得超过阈值
 * 3. 邮箱和密码匹配
 * @author Garin
 * @date 2020-08-09
 */
@Data
@EmailLoginValidation
public class EmailLoginRequest {
    /**
     * 邮箱地址
     */
    private String email;
    /**
     * 密码
     */
    private String password;
}
