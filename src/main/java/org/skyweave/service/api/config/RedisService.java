package org.skyweave.service.api.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class RedisService {

  @Autowired
  private RedisTemplate redisTemplate;
  @Autowired
  private ObjectMapper objectMapper;

  public <T> T get(String key, Class<T> entityClass) {
    try {
      Object value = redisTemplate.opsForValue().get(key);

      if (value == null) {
        return null;
      }

      // If value is already a string, parse it directly
      if (value instanceof String) {
        return objectMapper.readValue((String) value, entityClass);
      }

      // If value is an object, convert it
      return objectMapper.convertValue(value, entityClass);
    } catch (Exception e) {
      log.error("Error retrieving value from Redis", e);
      return null;
    }
  }

  public void set(String key, Object o, Long ttl) {
    try {
      redisTemplate.opsForValue().set(key, o, ttl, TimeUnit.SECONDS);
    } catch (Exception e) {
      log.error("Error storing value in Redis", e);
    }
  }


}
