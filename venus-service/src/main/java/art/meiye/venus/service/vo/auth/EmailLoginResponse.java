package art.meiye.venus.service.vo.auth;

import lombok.Data;

/**
 * @author Garin
 * @date 2020-08-11
 */

@Data
public class EmailLoginResponse {
    /**
     * 用户id
     */
    private Integer userId;
    /**
     * 用户id对应的token
     */
    private String token;
}
