package art.meiye.venus.service.vo.account;

import lombok.Data;
import art.meiye.venus.service.validate.annotations.EmailBindValidation;

/**
 * 账号绑定邮箱请求
 * @author Garin
 * @date 2020-09-05
 */
@Data
@EmailBindValidation
public class EmailBindRequest {
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
