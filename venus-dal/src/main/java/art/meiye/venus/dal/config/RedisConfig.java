package art.meiye.venus.dal.config;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * Redis连接配置
 * @author Garin
 * @date 2020-08-09
 */
@Configuration
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.timeout}")
    private int timeout;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private int minIdle;

    @Value("${spring.redis.jedis.pool.max-idle}")
    private int maxIdle;

    @Value("${spring.redis.jedis.pool.max-active}")
    private int maxActive;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private long maxWaitMillis;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.database}")
    private int database;

    @Bean
    public RedisTemplate createRedisTemplate(RedisConnectionFactory redisConnectionFactory){
        //1.创建对象
        RedisTemplate redisTemplate = new RedisTemplate();
        //2.设置连接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //3.设置redis生成的key的序列化器，对key编码进行处理
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        //4.返回
        return redisTemplate;
    }

    @Bean
    //配置spring提供的Redis连接池信息
    public GenericObjectPoolConfig createGenericObjectPoolConfig(){
        //1.创建Jedis连接池的配置对象
        GenericObjectPoolConfig genericObjectPoolConfig = new GenericObjectPoolConfig();
        //2.设置连接池信息
        genericObjectPoolConfig.setMaxTotal(maxActive);
        genericObjectPoolConfig.setMinIdle(minIdle);
        genericObjectPoolConfig.setMaxIdle(maxIdle);
        genericObjectPoolConfig.setMaxWaitMillis(maxWaitMillis);
        //3.返回
        return genericObjectPoolConfig;
    }

    @Bean
    //配置Redis标准连接配置对象
    public RedisStandaloneConfiguration createRedisStandaloneConfiguration(){
        //1.创建Redis服务器配置信息对象
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        //2.设置Redis服务器地址，端口和密码（如果有密码的话）
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(RedisPassword.of(password));
        //3.返回
        return redisStandaloneConfiguration;
    }


    @Bean
    public RedisConnectionFactory redisConnectionFactory(GenericObjectPoolConfig genericObjectPoolConfig,
                                                         RedisStandaloneConfiguration redisStandaloneConfig) {
        // 1.创建配置构建器，它是基于池的思想管理Jedis连接的
        JedisClientConfiguration jedisPoolConfig = JedisClientConfiguration
                .builder()
                .usePooling()
                .poolConfig(genericObjectPoolConfig)
                .build();
        // 2.创建Jedis连接工厂
        return new JedisConnectionFactory(redisStandaloneConfig, jedisPoolConfig);

    }

}
