package art.meiye.venus.common.api;

/**
 * 错误码
 * @author Garin
 * @date 2020-08-08
 */
public enum  ErrorMapping {
    /**
     * ErrorMapping - 错误码
     */

    AUTH_FAIL(1, "权限异常"),
    IDENTITY_EXPIRE(2, "登录过期，请重新登录"),
    EMAIL_NOT_VALID(100, "请输入正确的邮箱地址"),
    EMAIL_VERIFY_CODE_TOO_OFTEN(101, "验证码发送过于频繁，请稍后重试"),
    EMAIL_EXIST(102, "邮箱已注册，您可以直接进行登录"),
    /**
     * @see art.meiye.venus.common.utils.RegUtils#isValidPassword
     */
    PASSWORD_NOT_VALID(103, "密码要求：由6-16位字母数字下划线组成, 且必须包含一个字母和一个数字"),
    VERIFICATION_CODE_EMPTY(104, "验证码不能为空"),
    EMAIL_VERIFICATION_NOT_MATCH(105, "邮箱和验证码不匹配"),
    EMAIL_NOT_EXIST(106, "该邮箱尚未注册，请您进行注册"),
    LOGIN_ATTEMPT_FORBIDDEN(107, "登录失败次数过多"),
    IDENTITY_CREDENTIAL_NOT_MATCHES(108, "密码错误，请重新输入密码"),
    LOGIN_FORBIDDEN_PERMANENT(109, "您的账号已被永久禁用登录，请联系管理员解封"),
    VERIFICATION_CODE_VALIDATE_FORBIDDEN(110, "验证码错误次数过多，请12小时候后重试"),
    AUTH_FIELD_EMPTY(111, "必填字段为空"),
    AUTH_SK_INVALID(112, "sk错误"),
    AUTH_TOKEN_INVALID(114, "token错误"),
    /** 120 - 130 图片相关错误 */
    IMAGE_UPLOAD_FAIL(120, "图片上传失败"),
    IMAGE_UPLOAD_PARSE_ERROR_FAIL(121, "图片上传失败，解析错误"),
    /** 131 - 140 账户相关错误 */
    ACCOUNT_USER_NAME_ERROR(131, "用户名格式：4-30个字符，支持中英文、数字、“_”或者减号"),
    ACCOUNT_AVATAR_URL_ERROR(132, "图片URL非法"),
    ACCOUNT_WECHAT_BIND_UNEXPECT_ERROR(133, "微信账号绑定失败，请重试"),
    ACCOUNT_WECHAT_BIND_EXIST(134, "当前微信已经注册独立账号或已经绑定到其他账号下，请先进行解绑或注销由微信注册的账号再进行绑定"),
    ACCOUNT_WECHAT_LOGIN_ERROR(135, "微信登录失败，请重试"),
    ACCOUNT_WECHAT_UNBIND_INVALID(136, "当前账号只存在微信认证方式，无法解绑"),
    ACCOUNT_WECHAT_UNBIND_NOT_EXIST(137, "当前账号不存在微信认证方式，无法接解绑"),
    ACCOUNT_EMAIL_BIND_EXIST(138, "当前邮箱已经注册独立账号或已经绑定到其他账号下，请先进行解绑或注销由邮箱注册的账号再进行绑定"),
    ACCOUNT_EMAIL_UNBIND_INVALID(139, "当前账号只存在邮箱认证方式，无法解绑"),
    ACCOUNT_EMAIL_UNBIND_NOT_EXIST(140, "当前账号不存在邮箱认证方式，无法接解绑"),
    ;

    /** errorMsg - 返回的错误信息 */
    private final String errorMsg;
    /** errorMsg - 返回的错误码 */
    private final Integer errorCode;

    ErrorMapping(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public Integer getErrorCode() {
        return errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}
