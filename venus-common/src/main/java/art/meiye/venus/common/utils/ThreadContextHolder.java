package art.meiye.venus.common.utils;

import java.util.HashMap;

/**
 * RequestContextHolder
 * @author Garin
 * @date 2020-08-26
 */
public class ThreadContextHolder {
    /**
     * RequestId - 单个请求的traceId
     */
    private static final ThreadLocal<String> REQUEST_ID = new ThreadLocal<>();
    /**
     * userId - 用户id
     */
    private static final ThreadLocal<Integer> USER_ID = new ThreadLocal<>();
    /**
     * headers - 请求的Header
     */
    private static final ThreadLocal<HashMap<String, String>> HEADERS = new ThreadLocal<>();

    public static String getRequestId() {
        return REQUEST_ID.get();
    }

    public static void setRequestId(String requestId) {
        REQUEST_ID.set(requestId);
    }

    public static Integer getUserId() {
        return USER_ID.get();
    }

    public static void setUserId(Integer userId) {
        USER_ID.set(userId);
    }

    public static void setHeaders(HashMap<String, String> headers) {
        if (HEADERS.get() == null) {
            HEADERS.set(new HashMap<>());
        }
        HEADERS.get().putAll(headers);
    }

    public static HashMap<String, String> getHeaders() {
        if (HEADERS.get() == null) {
            return new HashMap<>();
        } else {
            return HEADERS.get();
        }
    }


    public static void clean() {
        HEADERS.remove();
        REQUEST_ID.remove();
        USER_ID.remove();
    }

}
