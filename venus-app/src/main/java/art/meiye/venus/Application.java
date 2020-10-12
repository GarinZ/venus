package art.meiye.venus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "art.meiye.venus")
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args).start();
    }
}
