import art.meiye.venus.Application;
import art.meiye.venus.sal.email.EmailSenderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Garin
 * @date 2020-08-06
 */

@ActiveProfiles("dev")
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class EmailSenderTest {
    @Autowired
    EmailSenderService emailSenderService;

    @Test
    public void sendEmailTest() {
        String targetAddress = "454067820@qq.com";
        String subject = "测试邮件";
        String text = "这是一个测试邮件";
        emailSenderService.sendMail(targetAddress, subject, text);
    }
}
