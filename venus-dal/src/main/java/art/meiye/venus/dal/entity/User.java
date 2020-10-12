package art.meiye.venus.dal.entity;

import java.util.Date;
import lombok.Data;

@Data
public class User {
    private Integer id;

    private String openid;

    private String unionid;

    private String webOpenid;

    private String publicOpenid;

    private String nickname;

    private Date createdAt;

    private Date updatedAt;

    private String province;

    private String city;

    private String country;

    private String avatarurl;

    private String plat;

    private Byte gender;

    private String language;

    private String sessionKey;

    private String secretKey;

    private Date vipExpiered;
}