package art.meiye.venus.controller.utils;

import art.meiye.venus.common.utils.CryptoUtils;

/**
 * 业务级 微信工具
 * @author Garin
 * @date 2020-09-01
 */
public class WechatUtils {
    private static final String SEPARATOR = "_";

    public static String getEncryptState(String sk, String staticKey) {
        return CryptoUtils.ascEncrypt(sk, staticKey);
    }

    public static String getEncryptState(String sk, String staticKey, Integer userId) {
        return CryptoUtils.ascEncrypt(sk, staticKey + SEPARATOR + userId.toString());
    }

    public static String getEncryptState(String sk, String staticKey, String referrer) {
        return CryptoUtils.ascEncrypt(sk, staticKey + SEPARATOR + referrer);
    }

    public static String getDecryptState(String sk, String encryptedState) {
        return CryptoUtils.ascDecrypt(sk, encryptedState);
    }

    public static Boolean isDecryptStateValid(String decryptedState, String staticKey) {
        return decryptedState.contains(staticKey);
    }

    public static Integer getUserIdFromState(String decryptedState) {
        String userIdStr = decryptedState.substring(decryptedState.indexOf(SEPARATOR) + 1);
        return Integer.valueOf(userIdStr);
    }

    public static String getReferrerFromState(String decryptedState) {
        int i = decryptedState.indexOf(SEPARATOR);
        if (decryptedState.length() - 1 == i) {
            return "";
        }
        return decryptedState.substring(decryptedState.indexOf(SEPARATOR) + 1);
    }
}
