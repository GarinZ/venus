package art.meiye.venus.common.utils;


import cn.hutool.core.util.RandomUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.crypto.symmetric.AES;

/**
 * @author Garin
 * @date 2020-08-11
 */
public class CryptoUtils {
    /**
     * 密码摘要加密方法
     * @param salt 盐
     * @param plaintext 明文
     * @return 密文
     */
    public static String md5Encrypt(String salt, String plaintext) {
        return DigestUtil.md5Hex(salt + DigestUtil.md5Hex(plaintext));
    }

    /**
     * AES对称加密
     * @param key 秘钥
     * @param data 明文数据
     * @return 加密数据
     */
    public static String ascEncrypt(String key, String data) {
        AES aes = SecureUtil.aes(key.getBytes());
        return aes.encryptHex(data);
    }

    /**
     * AES对称解密
     * @param key 秘钥
     * @param data 密文数据
     * @return 明文数据
     */
    public static String ascDecrypt(String key, String data) {
        AES aes = SecureUtil.aes(key.getBytes());
        return aes.decryptStr(data);
    }

    public static void main(String[] args) {
        String s = RandomUtil.randomString(16);
        System.out.println(s);
        System.out.println(md5Encrypt("wf0pfp98", "wokao123"));
    }
}
