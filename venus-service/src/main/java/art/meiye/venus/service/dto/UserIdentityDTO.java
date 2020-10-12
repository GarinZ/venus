package art.meiye.venus.service.dto;

import lombok.Data;

/**
 * 用户身份DTO
 * @author Garin
 * @date 2020-09-02
 */
@Data
public class UserIdentityDTO {
    private Integer userId;
    private String token;
}
