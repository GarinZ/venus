package art.meiye.venus.dal.redis.verificationcode.impl;

import art.meiye.venus.dal.redis.RedisKeyEnum;
import art.meiye.venus.dal.redis.verificationcode.VerificationCodeStorage;
import art.meiye.venus.dal.redis.verificationcode.VerificationEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * @author Garin
 * @date 2020-08-09
 */

@Service
public class VerificationCodeStorageImpl implements VerificationCodeStorage {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public String getVerificationCode(String identify, VerificationEnum verificationEnum) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        return operations.get(verificationEnum.getVerificationCode().getPrefix() + identify);
    }

    @Override
    public String setVerificationCode(String identify, String verificationCode, VerificationEnum verificationEnum) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        Duration duration = Duration.ofMillis(verificationEnum.getVerificationCode().getExpire());
        // TODO: 优化成pipeline事务型操作
        operations.set(verificationEnum.getVerificationCode().getPrefix() + identify, verificationCode, duration);
        lockVerificationCode(identify, verificationEnum);
        return verificationCode;
    }

    @Override
    public Boolean isVerificationCodeMatches(String identify, String code, VerificationEnum verificationEnum) {
        String codeInCache = getVerificationCode(identify, verificationEnum);
        return code != null && code.equals(codeInCache);
    }

    @Override
    public Boolean lockVerificationCode(String identify, VerificationEnum verificationEnum) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        Duration duration = Duration.ofMillis(verificationEnum.getCodeResetLock().getExpire());
        operations.set(verificationEnum.getCodeResetLock().getPrefix() + identify, "1", duration);
        return true;
    }

    @Override
    public Boolean isVerificationCodeAvailable(String identify, VerificationEnum verificationEnum) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        return operations.get(verificationEnum.getCodeResetLock().getPrefix() + identify) == null;
    }

    @Override
    public Long addValidateFailCounter(String identity, VerificationEnum verificationEnum) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        RedisKeyEnum validateCounter = verificationEnum.getValidateCounter();
        Duration duration = Duration.ofMillis(validateCounter.getExpire());
        stringRedisTemplate.expire(validateCounter.getPrefix() + identity, duration);
        return operations.increment(validateCounter.getPrefix() + identity);
    }

    @Override
    public Long getValidateFailCounter(String identity, VerificationEnum verificationEnum) {
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
        String counter = operations.get(verificationEnum.getValidateCounter().getPrefix() + identity);
        return counter == null ? 0L : Long.parseLong(counter);
    }

    @Override
    public void resetValidateFailCounter(String identity, VerificationEnum verificationEnum) {
        stringRedisTemplate.delete(verificationEnum.getValidateCounter().getPrefix() + identity);
    }
}
