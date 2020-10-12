package art.meiye.venus.common.domain;

/**
 * Cookie key 枚举
 * @author Garin
 * @date 2020-08-11
 */
public enum CookieEnum {
    /**
     * userid cookie
     */
    USER_ID("venus-userid"),
    /**
     * token cookie
     */
    TOKEN("venus-token"),
    ;

    private final String value;

    CookieEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    /**
     * 产品主域枚举
     */
    public enum Domain {
        /**
         * MEIYE *meiye.art全域
         * UXMAP *uxmap.cn全域
         */
        MEIYE(".meiye.art"),
        UX_MAP(".uxmap.cn"),
        PSKETCH(".psketch.net"),
        ;

        private final String domainUrl;

        Domain(String domainUrl) {
            this.domainUrl = domainUrl;
        }

        public String getDomainUrl() {
            return domainUrl;
        }
    }

    public static final String TEST_DOMAIN = ".test.com";
}
