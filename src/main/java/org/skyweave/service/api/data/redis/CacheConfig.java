package org.skyweave.service.api.data.redis;

import org.skyweave.service.api.data.model.User;
import org.skyweave.service.dto.PaginatedDigitalWorkDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class CacheConfig {

  @Bean
  public RedisCacheRepositoryImpl<PaginatedDigitalWorkDTO> digitalWorkCacheRepository(
          RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
    return new RedisCacheRepositoryImpl<>(redisTemplate, objectMapper, PaginatedDigitalWorkDTO.class);
  }

  @Bean
  public RedisCacheRepositoryImpl<User> userCacheRepository(
          RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
    return new RedisCacheRepositoryImpl<>(redisTemplate, objectMapper, User.class);
  }
}
