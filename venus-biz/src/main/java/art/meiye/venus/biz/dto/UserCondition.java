package art.meiye.venus.biz.dto;

import lombok.Data;

/**
 * 用户信息查询条件
 * @author Garin
 * @date 2020-08-26
 */
@Data
public class UserCondition {
    /**
     * 用户id
     */
    private Integer id;

    /**
     * 微信唯一id
     */
    private String unionId;
}
