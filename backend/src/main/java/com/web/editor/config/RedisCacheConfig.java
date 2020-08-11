package com.web.editor.config;

import com.web.editor.model.dto.user.bookmark.Bookmark;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class RedisCacheConfig {
// extends CachingConfigurerSupport{

    //출처 : https://velog.io/@minholee_93/Redis-CRUD-with-Spring-Boot-38k56bs96y
    private final RedisProperties redisProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(redisProperties.getHost());
        redisStandaloneConfiguration.setPort(redisProperties.getPort());
        redisStandaloneConfiguration.setPassword(redisProperties.getPassword());

        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        return lettuceConnectionFactory;
    }

    @Bean
    public RedisTemplate<Integer, Object> redisTemplateHash(){
        RedisTemplate<Integer, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        //출처 : https://m.blog.naver.com/PostView.nhn?blogId=cutesboy3&logNo=221469450441&proxyReferer=https:%2F%2Fwww.google.com%2F
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //객체를 json 형태로 깨지지 않고 받기 위한 직렬화 작업
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Bookmark.class));

        return redisTemplate;
    }

    
    @Bean
    public RedisTemplate<String, Object> redisTemplateSet(){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        //출처 : https://m.blog.naver.com/PostView.nhn?blogId=cutesboy3&logNo=221469450441&proxyReferer=https:%2F%2Fwww.google.com%2F
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //객체를 json 형태로 깨지지 않고 받기 위한 직렬화 작업
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Bookmark.class));

        return redisTemplate;
    }

    // @Bean
    // // @Primary
    // public CacheManager cacheManager2(RedisTemplate redisTemplate) {
    //     RedisCacheManager cacheManager = new RedisCacheManager(redisTemplate);
    //     // Number of seconds before expiration. Defaults to unlimited (0)
    //     cacheManager.setDefaultExpiration(20);
    //     cacheManager.setUsePrefix(true);
    //     return cacheManager;
    // }

    
    // @Bean
    // @Override
    // public CacheManager cacheManager(){
    //     RedisCacheManager.RedisCacheManagerBuilder builder = RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(redisConnectionFactory());
    //     RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
    //             .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
    //             .prefixKeysWith("imhere:")
    //             .entryTtl(Duration.ofHours(5L));
    //     builder.cacheDefaults(configuration);
    //     //출처 : https://yonguri.tistory.com/82
    //     return builder.build();
    // }

    // @Bean
    // public RedisTemplate<String, Object> redisTemplate(){
    //     RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    //     redisTemplate.setConnectionFactory(redisConnectionFactory());
    //     redisTemplate.setKeySerializer(new StringRedisSerializer());
    //     return redisTemplate;
    // }
}
