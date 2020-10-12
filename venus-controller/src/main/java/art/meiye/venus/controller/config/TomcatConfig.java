package art.meiye.venus.controller.config;

import art.meiye.venus.common.constants.EnvEnum;
import org.apache.tomcat.util.http.LegacyCookieProcessor;
import org.apache.tomcat.util.http.SameSiteCookies;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Tomcat配置类
 * @author Garin
 * @date 2020-08-26
 */
@Configuration
public class TomcatConfig {
    @Value("${env}")
    private String environment;
    /**
     * 允许Cookie设置".xxx.com"作为domain
     * @return 自定义Web服务器工厂
     */
    @Bean
    WebServerFactoryCustomizer<TomcatServletWebServerFactory> cookieProcessorCustomizer() {
        return tomcatServletWebServerFactory -> tomcatServletWebServerFactory.addContextCustomizers(
                context -> {
                    LegacyCookieProcessor legacyCookieProcessor = new LegacyCookieProcessor();
                    if (EnvEnum.PRODUCT.getValue().equals(environment)) {
                        legacyCookieProcessor.setSameSiteCookies(SameSiteCookies.NONE.getValue());
                    }
                    context.setCookieProcessor(legacyCookieProcessor);
                });
    }
}
