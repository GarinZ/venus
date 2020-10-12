package art.meiye.venus.dal.redis.auth.imlp;

import art.meiye.venus.dal.redis.RedisKeyEnum;
import art.meiye.venus.dal.redis.auth.AuthStorage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * @author Garin
 * @date 2020-08-11
 */
@Service
public class AuthStorageImpl implements AuthStorage {
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public void setToken(String identity, String token) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        Duration duration = Duration.ofMillis(RedisKeyEnum.USER_TOKEN.getExpire());
        operations.set(RedisKeyEnum.USER_TOKEN.getPrefix() + identity, token, duration);
    }

    @Override
    public boolean isTokenValid(String identity, String token) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        String correctToken = operations.get(RedisKeyEnum.USER_TOKEN.getPrefix() + identity);
        return token != null && token.equals(correctToken);
    }

    @Override
    public String getOrSetToken(String identity, String token) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        Duration duration = Duration.ofMillis(RedisKeyEnum.USER_TOKEN.getExpire());
        Boolean success = operations.setIfAbsent(RedisKeyEnum.USER_TOKEN.getPrefix() + identity, token, duration);
        return success != null && success ? token : operations.get(RedisKeyEnum.USER_TOKEN.getPrefix() + identity);
    }

    @Override
    public Long getAuthFailCounter(String identity) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        String increase = operations.get(RedisKeyEnum.AUTH_FAIL_COUNTER.getPrefix() + identity);
        if (increase == null) {
            return 0L;
        } else {
            return Long.valueOf(increase);
        }
    }

    @Override
    public Long addAuthFailCounter(String identity) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        Long increment = operations.increment(RedisKeyEnum.AUTH_FAIL_COUNTER.getPrefix() + identity);
        Duration duration = Duration.ofMillis(RedisKeyEnum.AUTH_FAIL_COUNTER.getExpire());
        stringRedisTemplate.expire(RedisKeyEnum.AUTH_FAIL_COUNTER.getPrefix() + identity, duration);
        return increment;
    }

    @Override
    public void resetAuthFailCounter(String identity) {
        stringRedisTemplate.delete(RedisKeyEnum.AUTH_FAIL_COUNTER.getPrefix() + identity);
    }

    @Override
    public void setAuthForbiddenLock(String identity, Long millisecond) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        Duration duration = Duration.ofMillis(millisecond);
        operations.set(RedisKeyEnum.AUTH_FORBIDDEN_LOCK.getPrefix() + identity, "1", duration);
    }

    @Override
    public Long hasAuthForbiddenLock(String identity) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        return stringRedisTemplate.getExpire(RedisKeyEnum.AUTH_FORBIDDEN_LOCK.getPrefix() + identity, TimeUnit.SECONDS);
    }
}
