package art.meiye.venus.service.vo.account;

import lombok.Data;

/**
 * 头像上传返回
 * @author Garin
 * @date 2020-08-27
 */
@Data
public class ImageUploadResponse {
    /**
     * 上传后生成的URL
     */
    private String url;
}
