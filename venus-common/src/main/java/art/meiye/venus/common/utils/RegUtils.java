package art.meiye.venus.common.utils;

import java.util.regex.Pattern;

/**
 * 正则表达式
 * @author Garin
 * @date 2020-08-11
 */
public class RegUtils {

    private static final Pattern EMAIL_REG = Pattern
            .compile("^[A-Za-z0-9\\u4e00-\\u9fa5_-]+@[a-zA-Z0-9_-]+(\\.[a-zA-Z0-9_-]+)+$");
    /**
     * 密码要求：由6-16位字母数字下划线组成, 且必须包含一个字母和一个数字
     */
    private static final Pattern PASSWORD_REG = Pattern.compile("^(?=.*[A-Za-z]+)(?=.*[0-9]+)\\w{6,16}$");
    /**
     * 用户名称正则表达式
     */
    private static final Pattern USER_NAME_REG = Pattern.compile("^[\\u4e00-\\u9fa5\\w-]{4,30}$");

    private static final Pattern CHINESE_CHAR_REG = Pattern.compile("[\u0391-\uFFE5]");

    public static boolean isValidEmail(String email) {
        return EMAIL_REG.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return PASSWORD_REG.matcher(password).matches();
    }

    public static boolean isValidUserName(String userName) {
        final int minLength = 4;
        final int maxLength = 30;
        if (userName == null) {
            return false;
        }
        Integer lengthByChar = getStringLengthByChar(userName);
        if (lengthByChar < minLength || lengthByChar > maxLength) {
            return false;
        }
        if (!USER_NAME_REG.matcher(userName).matches()) {
            return false;
        }
        return true;
    }

    public static Integer getStringLengthByChar(String str) {
        int valueLength = 0;
        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
        for (int i = 0; i < str.length(); i++) {
            String temp = str.substring(i, i + 1);
            if (CHINESE_CHAR_REG.matcher(temp).matches()) {
                valueLength += 2;
            } else {
                valueLength += 1;
            }
        }
        return valueLength;
    }
}
