package org.skyweave.service.api.data.redis;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Implementation of the redis cache repo for List of maps hash
 */

@RequiredArgsConstructor
public class RedisCacheRepositoryImpl<T> implements IRedisCacheRepository<T> {

  @Value("${redis.cache.ttl.skyweave:300}")
  private String ttl;

  private final RedisTemplate<String, Object> redisTemplate;
  private final ObjectMapper objectMapper;
  private final Class<T> type;

  @Override
  public T findByKey(String key) {
    Object cached = redisTemplate.opsForValue().get(key);
    if (cached == null) {
      return null;
    }
    return objectMapper.convertValue(cached, type);
  }

  @Override
  public void save(String key, T value) {
    redisTemplate.opsForValue().set(key, value);
    redisTemplate.expire(key, Long.parseLong(ttl), TimeUnit.SECONDS);
  }

  @Override
  public void delete(String key) {
    redisTemplate.delete(key);
  }
}
