package art.meiye.venus.sal.wechat.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 错误返回
 * @author Garin
 * @date 2020-09-01
 */
@Data
public class ErrorResponse {
    /**
     * 错误码
     */
    @JSONField(name = "errcode")
    private Integer errorCode;
    /**
     * 错误信息
     */
    @JSONField(name = "errmsg")
    private String errorMsg;
}
