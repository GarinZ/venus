package art.meiye.venus.dal.mapper;

import art.meiye.venus.dal.entity.UserIdentity;
import art.meiye.venus.dal.entity.UserIdentityExample;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserIdentityMapper {
    long countByExample(UserIdentityExample example);

    int deleteByExample(UserIdentityExample example);

    int deleteByPrimaryKey(Integer userIdentityId);

    int insert(UserIdentity record);

    int insertSelective(UserIdentity record);

    List<UserIdentity> selectByExample(UserIdentityExample example);

    UserIdentity selectByPrimaryKey(Integer userIdentityId);

    int updateByExampleSelective(@Param("record") UserIdentity record, @Param("example") UserIdentityExample example);

    int updateByExample(@Param("record") UserIdentity record, @Param("example") UserIdentityExample example);

    int updateByPrimaryKeySelective(UserIdentity record);

    int updateByPrimaryKey(UserIdentity record);
}