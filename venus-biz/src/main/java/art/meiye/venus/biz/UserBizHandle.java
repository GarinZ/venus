package art.meiye.venus.biz;

import art.meiye.venus.biz.dto.UserCondition;
import art.meiye.venus.dal.entity.User;
import org.springframework.util.StringUtils;

import java.util.regex.Pattern;

/**
 * 业务逻辑操作 - 用户
 * @author Garin
 * @date 2020-08-26
 */
public interface UserBizHandle {

    /** 默认用户昵称前缀 */
    String NICK_NAME_DEFAULT_PREFIX = "Genius";
    /** 默认用户昵称长度 */
    int NICK_NAME_DEFAULT_SUFFIX_LENGTH = 8;
    /** 默认头像 */
    String DEFAULT_AVATAR = "http://images.psketch.com/defaultAvatar.png";

    /**
     * 新增用户
     * @param user 用户实体
     * @return 新增用户记录
     */
    User addUser(User user);

    /**
     * 获取单个用户信息
     * @param userCondition 用户查询条件
     * @return 用户实体
     */
    User getSingleUserInfoByCondition(UserCondition userCondition);

    /**
     * 按需修改用户信息
     * @param user user实体
     */
    void updateUserSelective(User user);

    /**
     * 更新用户信息记录中的默认值
     * 该方法只会替换用户的默认值，非默认值不会替换
     * @param userId 用户id
     * @param userUpdate 更新成的记录
     */
    void updateDefaultUserInfo(Integer userId, User userUpdate);

    /**
     * 判断是否为系统默认昵称
     * @param nickName 昵称
     * @return 布尔值
     */
    static Boolean isDefaultNickName(String nickName) {
        if (StringUtils.isEmpty(nickName)) {
            return true;
        }
        Pattern defaultUserReg = Pattern.compile("^"
                + UserBizHandle.NICK_NAME_DEFAULT_PREFIX
                +"[A-Za-z0-9]{"
                + UserBizHandle.NICK_NAME_DEFAULT_SUFFIX_LENGTH +"}$");
        return defaultUserReg.matcher(nickName).matches();
    }

    /**
     * 判断是否为系统默认头像
     * @param avatarUrl 头像url
     * @return 布尔值
     */
    static Boolean isDefaultAvatarUrl(String avatarUrl) {
        if (StringUtils.isEmpty(avatarUrl)) {
            return true;
        }
        return UserBizHandle.DEFAULT_AVATAR.equals(avatarUrl);
    }
}
