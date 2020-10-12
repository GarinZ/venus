package art.meiye.venus.biz.dto;

import art.meiye.venus.common.domain.UserIdentityTypeEnum;
import lombok.Data;

import java.util.List;

/**
 * 用户身份查询条件 DTO
 * @author Garin
 * @date 2020-08-11
 */
@Data
public class UserIdentityCondition {
    private UserIdentityTypeEnum userIdentityType;
    private String identifier;
    private String credential;
    private List<Integer> userId;
}
