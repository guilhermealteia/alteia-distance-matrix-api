package br.com.alteia.common.configuration;

import br.com.alteia.common.adapters.handlers.CustomCacheErrorHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.io.IOException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Conditional(RedisCacheCondition.class)
@Configuration
@EnableCaching
public class RedisCacheConfig extends CachingConfigurerSupport {

    private static final Logger LOG = Logger.getLogger(RedisCacheConfig.class.getName());

    private static final Map<String, RedisCacheConfiguration> ttlConfigObjects = new HashMap<>();
    private static final Map<String, Long> timeoutsAndCommands = new HashMap<>();

    private static final String PATH_FILE_REDIS_TTL = "/redis-ttl.properties";//NOSONAR
    private static final String PATH_FILE_REDIS_CMD = "/redis-cmd-timeout.properties";

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

//    @Value("${spring.redis.password}")
//    private String password;

    @Value("${spring.redis.ssl:true}")
    private boolean useSsl;

    @Value("${spring.redis.expiretime:10}")
    private long defaultExpiration;

    @Value("${spring.redis.pool.max.total:100}")
    private int redisPoolMaxTotal;

    @Value("${spring.redis.pool.max.idle:100}")
    private int redisPoolMaxIdle;

    @Value("${spring.redis.pool.min.idle:50}")
    private int redisPoolMinIdle;

    @Value("${spring.redis.block.when.exhausted:true}")
    private boolean redisBlockWhenExhaustedValue;

    @Value("${spring.redis.max.wait.millis:1000}")
    private long redisMaxWaitMillis;

    @Value("${spring.redis.timeout:5000}")
    private long timeout;

    @Value("${spring.redis.ping.before.activate.connection:true}")
    private boolean redisPingBeforeActivateConnection;

    @Value("${spring.redis.event.emit.interval:10}")
    private long eventEmitInterval;


    @Value("${spring.redis.cluster.enabled:false}")
    private boolean redisClusterEnabled;

    @Value("${spring.redis.cluster.nodes:}")
    private List<String> redisClusterNodes;

//    @Value("${spring.redis.cluster.password:}")
//    private String redisClusterPassword;

    @Value("${spring.redis.cluster.timeout:5000}")
    private long redisClusterTimeout;

    @Value("${spring.redis.cluster.max-redirects:3}")
    private long redisClusterMaxRedirects;

    static {
        try {
            Properties properties = PropertiesLoaderUtils.loadProperties(new ClassPathResource(PATH_FILE_REDIS_TTL));
            ttlConfigObjects.putAll(
                    properties.entrySet().stream().collect(Collectors.toMap(
                            i -> i.getKey().toString(),
                            i -> RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(Long.parseLong((String) i.getValue()))))
                    )
            );

            Properties redisCommandByProperties = PropertiesLoaderUtils.loadProperties(new ClassPathResource(PATH_FILE_REDIS_CMD));
            timeoutsAndCommands.putAll(
                    redisCommandByProperties.entrySet().stream().collect(Collectors.toMap(
                            cmd -> cmd.getKey().toString(), cmd -> Long.parseLong(String.valueOf(cmd.getValue()))
                    ))
            );
        } catch (IOException e) {
            LOG.severe(String.format("Error when trying to load redis ttl or redis command timeout config file: %s", e.getMessage()));
        }
    }

    @Bean
    public RedisCacheManager redisCacheManager(LettuceConnectionFactory lettuceConnectionFactory) {
        JdkSerializationRedisSerializer redisSerializer = new JdkSerializationRedisSerializer(getClass().getClassLoader());

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofHours(defaultExpiration))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer));

        redisCacheConfiguration.usePrefix();

        RedisCacheManager redisCacheManager = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(lettuceConnectionFactory)
                .cacheDefaults(redisCacheConfiguration)
                .withInitialCacheConfigurations(ttlConfigObjects)
                .build();
        redisCacheManager.setTransactionAware(true);
        return redisCacheManager;
    }

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        return new LettuceConnectionFactory(new RedisStandaloneConfiguration(host, port));
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new CustomCacheErrorHandler();
    }
}