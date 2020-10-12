package art.meiye.venus.sal.upload;

import art.meiye.venus.common.api.enums.MetaStatus;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import com.qiniu.common.QiniuException;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * 上传服务
 * @author Garin
 * @date 2020-08-27
 */
@Service
@Slf4j
public class UploadService {
    @Value("${qiniu.accessKey}")
    private String accessKey;

    @Value("${qiniu.secretKey}")
    private String secretKey;

    @Value("${qiniu.urlPrefix}")
    private String urlPrefix;

    @Value("${qiniu.bucket}")
    private String bucket;

    @Autowired
    private OkHttpClient okHttpClient;

    /**
     * 文件上传服务
     * @param bytes 字节数组
     * @return URL
     */
    public Optional<String> uploadFile(byte[] bytes) {
        Digester md5 = new Digester(DigestAlgorithm.MD5);
        String fileName = md5.digestHex(bytes);
        UploadManager uploadManager = new UploadManager(new Configuration());
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket, fileName);
        try {
            uploadManager.put(bytes, fileName, upToken);
        } catch (QiniuException e) {
            log.error("图片上传七牛云失败", e);
            return Optional.empty();
        }
        return Optional.of(urlPrefix + fileName);
    }

    public Optional<String> uploadImageFromUrl(String url) {
        Request request = new Request.Builder().url(url).build();
        Call call = okHttpClient.newCall(request);
        Response response = null;
        try {
            response = call.execute();
            if (!MetaStatus.SUCCESS.getCode().equals(response.code())) {
                log.error("请求图片URL失败\t状态码异常\t{}", response);
                return Optional.empty();
            }
            byte[] bytes = Objects.requireNonNull(response.body()).bytes();
            return uploadFile(bytes);
        } catch (IOException e) {
            log.error("请求图片URL失败\tI/O异常", e);
            return Optional.empty();
        }
    }
}
