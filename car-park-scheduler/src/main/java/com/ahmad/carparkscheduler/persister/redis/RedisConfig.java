package com.ahmad.carparkscheduler.persister.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

  @Bean
  public ReactiveRedisTemplate<String, CarParkGeoData> reactiveRedisTemplateConfig(
      final ReactiveRedisConnectionFactory factory) {
    // Define key serializer
    final StringRedisSerializer keySerializer = new StringRedisSerializer();

    // Define value serializer
    final Jackson2JsonRedisSerializer<CarParkGeoData> serializer = new Jackson2JsonRedisSerializer<>(
        CarParkGeoData.class);

    // Build the RedisSerializationContext
    final RedisSerializationContext.RedisSerializationContextBuilder<String, CarParkGeoData> builder =
        RedisSerializationContext.newSerializationContext(keySerializer);

    final RedisSerializationContext<String, CarParkGeoData> context = builder
        .value(serializer)
        .build();

    return new ReactiveRedisTemplate<>(factory, context);
  }
}
