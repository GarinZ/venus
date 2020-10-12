package art.meiye.venus.biz.impl;

import art.meiye.venus.biz.UserBizHandle;
import art.meiye.venus.biz.dto.UserCondition;
import art.meiye.venus.dal.entity.User;
import art.meiye.venus.dal.entity.UserExample;
import art.meiye.venus.dal.mapper.UserMapper;
import art.meiye.venus.sal.upload.UploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Garin
 * @date 2020-08-26
 */
@Service
public class UserBizHandleImpl implements UserBizHandle {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UploadService uploadService;

    @Override
    public User addUser(User user) {
        userMapper.insertSelective(user);
        return user;
    }

    @Nullable
    @Override
    public User getSingleUserInfoByCondition(UserCondition userCondition) {
        UserExample example = new UserExample();
        UserExample.Criteria criteria = example.createCriteria();
        if (userCondition.getId() != null) {
            criteria.andIdEqualTo(userCondition.getId());
        }
        if (userCondition.getUnionId() != null) {
            criteria.andUnionidEqualTo(userCondition.getUnionId());
        }
        List<User> users = userMapper.selectByExample(example);
        if (users == null || CollectionUtils.isEmpty(users)) {
            return null;
        }
        return users.get(0);
    }

    @Override
    public void updateUserSelective(User user) {
        userMapper.updateByPrimaryKeySelective(user);
    }

    @Override
    public void updateDefaultUserInfo(Integer userId, User userUpdate) {
        UserCondition condition = new UserCondition();
        condition.setId(userId);
        User oldUser = getSingleUserInfoByCondition(condition);
        User newUser = new User();
        if (UserBizHandle.isDefaultNickName(oldUser.getNickname()) && !StringUtils.isEmpty(userUpdate.getNickname())) {
            newUser.setNickname(userUpdate.getNickname());
        }
        if (UserBizHandle.isDefaultAvatarUrl(oldUser.getAvatarurl()) && !StringUtils.isEmpty(userUpdate.getAvatarurl())) {
            Optional<String> imgUrlOpt = uploadService.uploadImageFromUrl(userUpdate.getAvatarurl());
            imgUrlOpt.ifPresent(newUser::setAvatarurl);
        }
        if (oldUser.getGender() == null && userUpdate.getGender() != null) {
            newUser.setGender(userUpdate.getGender());
        }
        if (StringUtils.isEmpty(oldUser.getCity()) && !StringUtils.isEmpty(userUpdate.getCity())) {
            newUser.setCity(userUpdate.getCity());
        }
        if (StringUtils.isEmpty(oldUser.getCountry()) && !StringUtils.isEmpty(userUpdate.getCountry())) {
            newUser.setCountry(userUpdate.getCountry());
        }
        if (StringUtils.isEmpty(oldUser.getProvince()) && !StringUtils.isEmpty(userUpdate.getProvince())) {
            newUser.setProvince(userUpdate.getProvince());
        }
        newUser.setPlat(userUpdate.getPlat());
        newUser.setWebOpenid(userUpdate.getWebOpenid());
        newUser.setUnionid(userUpdate.getUnionid());
        newUser.setLanguage(userUpdate.getLanguage());
        newUser.setId(oldUser.getId());
        updateUserSelective(newUser);
    }
}
