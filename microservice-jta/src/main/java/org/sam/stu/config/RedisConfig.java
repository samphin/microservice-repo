package org.sam.stu.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.*;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

    /**
     * key生成策略是SpringCache的内容，与缓存器产品无关。 key生成策略 target: 类 method: 方法 params: 参数
     *
     * @return KeyGenerator 注意: 该方法只是声明了key的生成策略,还未被使用,
     * 需在@Cacheable注解中指定keyGenerator，如: @Cacheable(value =
     * "key",keyGenerator = "keyGenerator")
     */
    @Bean
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> {
            StringBuilder sb = new StringBuilder();
            /*
             * sb.append(target.getClass().getSimpleName()); sb.append(method.getName());
             */
            for (Object obj : params) {
                // 由于参数可能不同, hashCode肯定不一样, 缓存的key也需要不一样
                // sb.append(JSON.toJSONString(obj).hashCode());
                sb.append(obj).toString();
            }
            return sb.toString();
        };
    }

    /**
     * redis全局默认配置
     * springboot1.5x版 redis配置
     * @param redisTemplate
     * @return
     */
	/*@Bean
	public CacheManager cacheManager(RedisTemplate<String, Object> redisTemplate) {
		RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
		redisCacheManager.setUsePrefix(true);
		// key缓存的过期时间, 600秒
		redisCacheManager.setDefaultExpiration(600L);
		return redisCacheManager;
	}*/

    /**
     * springboot2.0版 redis配置
     * @param connectionFactory
     * @return
     */

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        //初始化一个
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(connectionFactory);
        /*设置CacheManager的值序列化方式为JdkSerializationRedisSerializer,
        但其实RedisCacheConfiguration默认就是使用StringRedisSerializer序列化key，
        JdkSerializationRedisSerializer序列化value, 所以以下注释代码为默认实现*/
        ClassLoader loader = this.getClass().getClassLoader();
        JdkSerializationRedisSerializer jdkSerializer = new JdkSerializationRedisSerializer(loader);
        RedisSerializationContext.SerializationPair<Object> pair =
        RedisSerializationContext.SerializationPair.fromSerializer(jdkSerializer);
        RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig().
                serializeValuesWith(pair);
        //RedisCacheConfiguration defaultCacheConfig = RedisCacheConfiguration.defaultCacheConfig(); //设置默认超过期时间是30秒
        defaultCacheConfig.entryTtl(Duration.ofSeconds(30)); //初始化RedisCacheManager
        RedisCacheManager cacheManager = new RedisCacheManager(redisCacheWriter,
                defaultCacheConfig);
        return cacheManager;
    }

    /**
     * RedisTemplate对象配置信息
     *
     * @param factory
     * @return
     */
//	@Bean
//	public RedisTemplate<String, Object> getRedisTemplate(RedisConnectionFactory factory) {
//		RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
//		redisTemplate.setConnectionFactory(factory);
//		// key序列化方式,但是如果方法上有Long等非String类型的话，会报类型转换错误
//		// 所以在没有自己定义key生成策略的时候，以下这个代码建议不要这么写，可以不配置或者自己实现ObjectRedisSerializer
//		/*
//		 * RedisSerializer<String> redisSerializer = new StringRedisSerializer();//
//		 * Long类型不可以会出现异常信息; redisTemplate.setKeySerializer(redisSerializer);
//		 */
//		// 采用StringRedisSerializer来对key进行序列化
//		RedisSerializer<String> redisSerializer = new StringRedisSerializer();
//		redisTemplate.setKeySerializer(redisSerializer);
//		// 自定义Value序列化策略
//		Jackson2JsonRedisSerializer<?> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//		ObjectMapper om = new ObjectMapper();
//		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//		jackson2JsonRedisSerializer.setObjectMapper(om);
//		redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
//		redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
//		redisTemplate.afterPropertiesSet();
//		return redisTemplate;
//	}

    /**
     * 重写Redis序列化方式，使用Json方式:
     * 当我们的数据存储到Redis的时候，我们的键（key）和值（value）都是通过Spring提供的Serializer序列化到数据库的。RedisTemplate默认使用的是JdkSerializationRedisSerializer，StringRedisTemplate默认使用的是StringRedisSerializer。
     * Spring Data JPA为我们提供了下面的Serializer：
     * GenericToStringSerializer、Jackson2JsonRedisSerializer、JacksonJsonRedisSerializer、JdkSerializationRedisSerializer、OxmSerializer、StringRedisSerializer。
     * 在此我们将自己配置RedisTemplate并定义Serializer。
     *
     * @param redisConnectionFactory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 禁用默认的序列化策略
        redisTemplate.setEnableDefaultSerializer(false);

        // 手动定义Key序列化策略（用StringRedisSerializer,必须保证Key是String类型，如果是int、long会报错 ）
        // 设置键（key）的序列化采用StringRedisSerializer。
        RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringRedisSerializer);
        redisTemplate.setHashKeySerializer(stringRedisSerializer);
//        JdkSerializationRedisSerializer jdkSerializationRedisSerializer = new JdkSerializationRedisSerializer();
        // GenericJackson2JsonRedisSerializer jackson2JsonRedisSerializer = new
        // GenericJackson2JsonRedisSerializer();
        Jackson2JsonRedisSerializer<?> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
        ObjectMapper om = new ObjectMapper();
        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        jackson2JsonRedisSerializer.setObjectMapper(om);
        // 设置值（value）的序列化采用FastJsonRedisSerializer。
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
