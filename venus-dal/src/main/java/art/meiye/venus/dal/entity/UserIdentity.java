package art.meiye.venus.dal.entity;

import lombok.Data;

@Data
public class UserIdentity {
    private Integer userIdentityId;

    private Integer userId;

    private Integer userIdentityType;

    private String identifier;

    private String credential;

    private Integer isDelete;

    private String credentialSalt;
}