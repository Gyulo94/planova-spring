package com.planova.server.global.util;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class RedisUtils {

  private final RedisTemplate<String, String> redisTemplate;

  public RedisUtils(RedisTemplate<String, String> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public void set(String key, String value, Long expiredTime) {
    if (expiredTime != null) {
      redisTemplate.opsForValue().set(key, value, expiredTime, TimeUnit.SECONDS);
    } else {
      redisTemplate.opsForValue().set(key, value);
    }
  }

  public String get(String key) {
    return redisTemplate.opsForValue().get(key);
  }

  public void del(String key) {
    redisTemplate.delete(key);
  }

  public Boolean hasKey(String key) {
    return redisTemplate.hasKey(key);
  }
}
