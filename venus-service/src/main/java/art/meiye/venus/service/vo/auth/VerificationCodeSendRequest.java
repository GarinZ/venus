package art.meiye.venus.service.vo.auth;

import art.meiye.venus.service.validate.annotations.VerificationCodeThreshold;
import lombok.Data;

/**
 * VerificationCodeSendRequest
 * @author Garin
 * @date 2020-08-06
 */
@Data
public class VerificationCodeSendRequest {
    /**
     * 邮箱地址
     */
    @VerificationCodeThreshold
    private String email;
}
