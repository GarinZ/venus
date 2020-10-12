import art.meiye.venus.Application;
import art.meiye.venus.dal.redis.verificationcode.VerificationCodeStorage;
import art.meiye.venus.dal.redis.verificationcode.VerificationEnum;
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
public class VerificationCodeStorageTest {
    @Autowired
    VerificationCodeStorage verificationCodeStorage;

    @Test
    public void lockVerificationCode() throws InterruptedException {
        String email = "454067820@qq.com";
        // 加锁成功
        assert verificationCodeStorage.lockVerificationCode(email, VerificationEnum.EMAIL);
        // 加锁失败
        assert !verificationCodeStorage.lockVerificationCode(email, VerificationEnum.EMAIL);
    }
}
