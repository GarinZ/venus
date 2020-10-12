import art.meiye.venus.common.utils.RegUtils;

/**
 * @author Garin
 * @date 2020-08-11
 */
public class RegUtilsTest {
    public static void main(String[] args) {
        // 邮箱正则测试
        assert RegUtils.isValidEmail("454067820@qq.com");
        assert !RegUtils.isValidEmail("454067820qq.com");
        assert !RegUtils.isValidEmail("454067820.com");
        assert !RegUtils.isValidEmail("454067820@com");
        assert RegUtils.isValidEmail("454067820@meiye.art");
        // 密码正则测试
        // 长度不足
        assert !RegUtils.isValidPassword("testt");
        // 只为数字
        assert !RegUtils.isValidPassword("12312321");
        // 只为字母
        assert !RegUtils.isValidPassword("abcabc");
        // 只有下划线
        assert !RegUtils.isValidPassword("______");
        // 包含不合法字符
        assert !RegUtils.isValidPassword("@@@@@@");
        // 正确
        assert RegUtils.isValidPassword("abc123");
    }
}
