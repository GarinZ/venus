package art.meiye.venus.service.vo.account;

import lombok.Data;

/**
 * 返回 - 用户档案
 * @author Garin
 * @date 2020-08-26
 */
@Data
public class UserProfileResponse {
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 用户头像
     */
    private String avatarUrl;
    /**
     * 是否绑定微信账号
     */
    private Boolean isWechatBind;
    /**
     * 是否绑定邮箱账号
     */
    private Boolean isEmailBind;
}
