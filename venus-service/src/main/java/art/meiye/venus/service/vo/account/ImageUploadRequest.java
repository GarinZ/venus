package art.meiye.venus.service.vo.account;

import lombok.Data;

/**
 * 头像上传请求
 * @author Garin
 * @date 2020-08-27
 */
@Data
public class ImageUploadRequest {
    /**
     * base64编码
     */
    private String base64;
}
