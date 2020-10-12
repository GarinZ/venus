import art.meiye.venus.Application;
import art.meiye.venus.dal.redis.RedisResponseEnum;
import art.meiye.venus.dal.redis.auth.AuthStorage;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Garin
 * @date 2020-08-09
 */
@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class AuthStorageTest {
    @Autowired
    AuthStorage authStorage;

    @Test
    public void authForbiddenTest() {
        String email = "454067820@qq.com";
        authStorage.setAuthForbiddenLock(email, 50000L);
        assert authStorage.hasAuthForbiddenLock(email) > 0;
        assert RedisResponseEnum.EXPIRE_EMPTY.getValue()
                .equals(authStorage.hasAuthForbiddenLock("test").toString());
    }
}
