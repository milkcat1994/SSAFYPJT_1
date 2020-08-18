package com.web.editor.config;

import com.web.editor.model.dto.user.bookmark.Bookmark;
import com.web.editor.model.dto.user.search.SearchPortfolioJoinBookmark;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class RedisCacheConfig {

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
    public RedisTemplate<String, Object> redisTemplateHash(){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        //출처 : https://m.blog.naver.com/PostView.nhn?blogId=cutesboy3&logNo=221469450441&proxyReferer=https:%2F%2Fwww.google.com%2F
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //객체를 json 형태로 깨지지 않고 받기 위한 직렬화 작업
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(SearchPortfolioJoinBookmark.class));

        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, Integer> redisTemplateHashUid(){
        RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        //출처 : https://m.blog.naver.com/PostView.nhn?blogId=cutesboy3&logNo=221469450441&proxyReferer=https:%2F%2Fwww.google.com%2F
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //객체를 json 형태로 깨지지 않고 받기 위한 직렬화 작업
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Integer.class));

        return redisTemplate;
    }

    
    @Bean
    public RedisTemplate<String, String> redisTemplateSet(){
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        //출처 : https://m.blog.naver.com/PostView.nhn?blogId=cutesboy3&logNo=221469450441&proxyReferer=https:%2F%2Fwww.google.com%2F
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //객체를 json 형태로 깨지지 않고 받기 위한 직렬화 작업
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(String.class));

        return redisTemplate;
    }

    
    @Bean
    public RedisTemplate<String, Object> redisTemplate(){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        //출처 : https://m.blog.naver.com/PostView.nhn?blogId=cutesboy3&logNo=221469450441&proxyReferer=https:%2F%2Fwww.google.com%2F
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //객체를 json 형태로 깨지지 않고 받기 위한 직렬화 작업
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Bookmark.class));

        return redisTemplate;
    }
}
