package com.recipepicker.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.recipepicker.entity.RecipeEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, RecipeEntity> recipeRedisTemplate(
            RedisConnectionFactory connectionFactory,  ObjectMapper objectMapper) {

        RedisTemplate<String, RecipeEntity> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        StringRedisSerializer keySerializer = new StringRedisSerializer();
        Jackson2JsonRedisSerializer<RecipeEntity> valueSerializer =
                new Jackson2JsonRedisSerializer<>(objectMapper, RecipeEntity.class);

        template.setKeySerializer(keySerializer);
        template.setHashKeySerializer(keySerializer);
        template.setValueSerializer(valueSerializer);
        template.setHashValueSerializer(valueSerializer);

        template.afterPropertiesSet();
        return template;
    }
}