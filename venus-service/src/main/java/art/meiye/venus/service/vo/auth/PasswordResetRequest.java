package art.meiye.venus.service.vo.auth;

import art.meiye.venus.service.validate.annotations.PasswordResetValidation;

import lombok.Data;

/**
 * 密码重置请求
 * @author Garin
 * @date 2020-08-14
 */
@Data
@PasswordResetValidation
public class PasswordResetRequest {
    /**
     * 邮箱
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
