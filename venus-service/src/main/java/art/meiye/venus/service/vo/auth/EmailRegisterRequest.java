package art.meiye.venus.service.vo.auth;

import art.meiye.venus.service.validate.annotations.EmailRegisterValidation;
import lombok.Data;

/**
 * 邮箱注册请求Vo
 *
 * 校验规则：
 * 1. 邮箱合法，且没有被注册
 * 2. 密码6-18位字母，数字，下划线
 * 3. 邮箱和验证码一致
 * @author Garin
 * @date 2020-08-09
 */
@Data
@EmailRegisterValidation
public class EmailRegisterRequest {
    /**
     * 邮箱地址
     */
    private String email;
    /**
     * 密码
     */
    private String password;
    /**
     * 验证码
     */
    private String verificationCode;
}
