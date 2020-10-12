package art.meiye.venus.dal.utils;

import art.meiye.venus.dal.entity.User;
import art.meiye.venus.sal.wechat.dto.UserProfileQueryResponse;

/**
 * {@link art.meiye.venus.dal.entity.User}的工具类
 * 由于Entity均为mybatis generator生成，因此无法将方法写在entity当中。
 * 因此将所有伴生类/工具类写在utils下
 *
 * @author Garin
 * @date 2020-09-02
 */
public class UserUtils {

    public static User buildUser(Integer userId, UserProfileQueryResponse response) {
        User user = new User();
        user.setId(userId);
        user.setUnionid(response.getUnionId());
        user.setNickname(response.getNickname());
        user.setProvince(response.getProvince());
        user.setCity(response.getCity());
        user.setCountry(response.getCountry());
        user.setGender(response.getSex().byteValue());
        user.setAvatarurl(response.getHeadImgUrl());
        user.setWebOpenid(response.getOpenId());
        user.setPlat("web");
        user.setLanguage("zh_CN");
        return user;
    }

    public static User buildUser(UserProfileQueryResponse response) {
        return buildUser(null, response);
    }
}
