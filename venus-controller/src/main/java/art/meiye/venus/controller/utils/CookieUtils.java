package art.meiye.venus.controller.utils;

import art.meiye.venus.common.constants.Constants;
import art.meiye.venus.common.constants.EnvEnum;
import art.meiye.venus.common.domain.CookieEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Cookie工具集
 * 由于Cookie类和Web服务器选型耦合，比如我们用的Tomcat，所以放在这里
 * @author Garin
 * @date 2020-08-26
 */
@Component
public class CookieUtils {
    @Value("${env}")
    private String environment;

    /**
     * 获取新的Cookie实例列表，每次均会实例化新的Cookie
     * @param userId 用户名
     * @param token token
     * @return Cookie实例列表
     */
    public List<Cookie> newCookieInstanceList(Integer userId, String token) {
        List<Cookie> cookies = new ArrayList<>();

        if (EnvEnum.DEV.getValue().equals(environment)) {
            Cookie userIdCookie = new Cookie(CookieEnum.USER_ID.getValue(), userId.toString());
            Cookie tokenCookie = new Cookie(CookieEnum.TOKEN.getValue(), token);
            buildCookie(userIdCookie, CookieEnum.TEST_DOMAIN, false);
            buildCookie(tokenCookie, CookieEnum.TEST_DOMAIN, false);
            cookies.add(userIdCookie);
            cookies.add(tokenCookie);
            return cookies;
        }
        for (CookieEnum.Domain domain : CookieEnum.Domain.values()) {
            Cookie userIdCookie = new Cookie(CookieEnum.USER_ID.getValue(), userId.toString());
            buildCookie(userIdCookie, domain.getDomainUrl(), true);
            Cookie tokenCookie = new Cookie(CookieEnum.TOKEN.getValue(), token);
            buildCookie(tokenCookie, domain.getDomainUrl(), true);
            cookies.add(userIdCookie);
            cookies.add(tokenCookie);
        }
        return cookies;
    }

    public List<Cookie> clearCookie() {
        List<Cookie> cookies = new ArrayList<>();
        if (EnvEnum.DEV.getValue().equals(environment)) {
            Cookie userIdCookie = new Cookie(CookieEnum.USER_ID.getValue(), null);
            Cookie tokenCookie = new Cookie(CookieEnum.TOKEN.getValue(), null);
            buildClearedCookie(userIdCookie, CookieEnum.TEST_DOMAIN, false);
            buildClearedCookie(tokenCookie, CookieEnum.TEST_DOMAIN, false);
            cookies.add(userIdCookie);
            cookies.add(tokenCookie);
            return cookies;
        }
        for (CookieEnum.Domain domain : CookieEnum.Domain.values()) {
            Cookie userIdCookie = new Cookie(CookieEnum.USER_ID.getValue(), null);
            buildClearedCookie(userIdCookie, domain.getDomainUrl(), true);
            Cookie tokenCookie = new Cookie(CookieEnum.TOKEN.getValue(), null);
            buildClearedCookie(tokenCookie, domain.getDomainUrl(), true);
            cookies.add(userIdCookie);
            cookies.add(tokenCookie);
        }
        return cookies;
    }

    /**
     * 建造cookie实例
     * @param cookie 待build的cookie实例
     * @param domainUrl 装载的Domain
     */
    public static void buildCookie(Cookie cookie, String domainUrl, Boolean secure) {
        final int maxAge = Constants.TOKEN_EXPIRE_DAY.intValue() * 24 * 3600;
        cookie.setMaxAge(maxAge);
        cookie.setPath("/");
        cookie.setDomain(domainUrl);
        cookie.setSecure(secure);
    }

    public static void buildClearedCookie(Cookie cookie, String domainUrl, Boolean secure) {
        cookie.setMaxAge(0);
        cookie.setPath("/");
        cookie.setDomain(domainUrl);
        cookie.setSecure(secure);
    }

    /**
     * 根据Cookie.name获取Cookie.value
     * @param cookieName name
     * @param cookies cookie数组
     * @return value
     */
    public static Optional<String> getCookieItemByName(CookieEnum cookieName, Cookie[] cookies) {
        if (cookies == null) {
            return Optional.empty();
        }
        for (Cookie cookie : cookies) {
            if (cookieName.getValue().equals(cookie.getName())) {
                return Optional.of(cookie.getValue());
            }
        }
        return Optional.empty();
    }
}
