package art.meiye.venus.service.vo.account;

import lombok.Data;
import art.meiye.venus.service.validate.annotations.UserProfileUpdateValidation;

/**
 * 返回 - 用户档案
 * @author Garin
 * @date 2020-08-26
 */
@Data
@UserProfileUpdateValidation
public class UserProfileUpdateRequest {
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 用户头像
     */
    private String avatarUrl;
}
