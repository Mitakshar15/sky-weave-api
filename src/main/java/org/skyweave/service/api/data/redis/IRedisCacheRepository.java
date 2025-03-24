package org.skyweave.service.api.data.redis;

import java.util.Map;

public interface IRedisCacheRepository<T> {

  T findByKey(String key);

  void save(String key, T value);

  void delete(String key);
}
